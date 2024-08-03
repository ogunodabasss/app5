package com.example.app5.util;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.util.Pair;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class GithubApiUtil {

    private static final ThreadPoolExecutor THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(), Runtime.getRuntime().availableProcessors() * 2, 100, TimeUnit.MINUTES, new LinkedBlockingQueue<>());
    private static final Pair<String, Byte> PARAM_PER_PAGE = Pair.of("per_page", (byte) 100);
    private static final String PARAM_PAGE_KEY = "page";
    private static final Pair<String, String> HEADER_GITHUB_API_ACCEPT = Pair.of("Accept", "application/vnd.github+json");
    private static final Pair<String, String> HEADER_GITHUB_API_VERSION = Pair.of("X-GitHub-Api-Version", "2022-11-28");
    private static final String HEADER_OUT_LINK = "Link";


    public static Map<String, List<CommitApi.CommitDto>> getLastOneMountCommit(final String userName, final LocalDateTime since, final LocalDateTime until) {
        final var repoNames = ReposApi.getUserRepos(userName);
        // new String[]{Arrays.stream(repoNames).findFirst().orElseThrow()}
        return CommitApi.getReposLastOneMount(userName, new String[]{Arrays.stream(repoNames).findFirst().orElseThrow()}, since, until);
    }

    @Slf4j
    public static class CommitDetailApi {
        // USER_NAME - REPO - SHA
        private static final String GITHUB_API_REPO_COMMIT_DETAILS = "https://api.github.com/repos/%s/%s/commits/%s";


        private static CommitDetailDto getReposCommitDetail(String userName, String repoName, String sha) {
            final Map<String, String> inputHeaders = new HashMap<>();
            inputHeaders.put(HEADER_GITHUB_API_ACCEPT.getFirst(), HEADER_GITHUB_API_ACCEPT.getSecond());
            inputHeaders.put(HEADER_GITHUB_API_VERSION.getFirst(), HEADER_GITHUB_API_VERSION.getSecond());

            final String URI = GITHUB_API_REPO_COMMIT_DETAILS.formatted(userName, repoName, sha);

            final Map<String, String> parameters = new HashMap<>();
            parameters.put(PARAM_PER_PAGE.getFirst(), String.valueOf(PARAM_PER_PAGE.getSecond()));

            Map<String, String> outputHeaders = new HashMap<>();
            outputHeaders.put(HEADER_OUT_LINK, null);

            final String jsonFirstPage = RequestUtil.getRequest(URI, parameters, inputHeaders, outputHeaders);
            final String headerLinkValue = outputHeaders.get(HEADER_OUT_LINK);
            final CommitDetailDto commitDetailDto = JsonUtil.jsonToDto(jsonFirstPage);

            if (headerLinkValue != null) throw new RuntimeException();
            /*
            if (HeaderLinkUtil.isPaging(headerLinkValue)) {
                final int size = HeaderLinkUtil.lastPageNumber(headerLinkValue);

                @SuppressWarnings("unchecked")
                CompletableFuture<String>[] completableFutures = new CompletableFuture[size - 1];
                for (int i = 2; i <= size; i++) {
                    int temp = i;
                    completableFutures[i - 2] = CompletableFuture.supplyAsync(() -> getReposCommitDetailNext(userName, repoName, sha, temp), THREAD_POOL_EXECUTOR);
                }
                CompletableFuture<Void> completableFuture = CompletableFuture.allOf(completableFutures).thenApply(_ -> {
                    for (int index = 0; index < size - 1; index++) {
                        String json = completableFutures[index].exceptionally(throwable -> {
                            log.error(throwable.getMessage(), throwable);
                            return "";
                        }).join();
                        commitDetailDtoList.addAll(JsonUtil.jsonToDto(json));
                    }
                    return null;
                });
                var _ = completableFuture.join();
            }

             */

            return commitDetailDto;
        }

        private static class JsonUtil {
            @SneakyThrows
            public static CommitDetailDto jsonToDto(final String json) {
                final JsonNode nodeRoot = JacksonUtil.MAPPER.readTree(json);
                if (nodeRoot == null || nodeRoot.isEmpty() || nodeRoot.isNull())
                    throw new RuntimeException();
                CommitDetailDto commitDetailDto;
                final String hash = nodeRoot.at("/sha").asText();
                final JsonNode nodeCommit = nodeRoot.at("/commit");
                final JsonNode nodeAuthor = nodeCommit.at("/author");
                final String author = nodeAuthor.at("/name").asText();
                final LocalDateTime dateTime = LocalDateTime.parse(nodeAuthor.at("/date").asText(), DateTimeFormatter.ISO_DATE_TIME);
                final String message = nodeCommit.at("/message").asText();

                final ArrayNode nodeFilesArray = nodeRoot.at("/files").withArray("");
                StringBuilder sbPatch = new StringBuilder();
                for (JsonNode nodeFile : nodeFilesArray) {
                    final String patch = nodeFile.at("/patch").asText();
                    sbPatch.append(patch);
                }
                commitDetailDto = new CommitDetailDto(hash, dateTime, author, message, sbPatch.toString());


                return commitDetailDto;
            }

        }

        public record CommitDetailDto(String hash, LocalDateTime time, String author, String message, String patch) {
        }
    }

    @Slf4j
    public static class CommitApi {

        // USER_NAME - REPO
        private static final String GITHUB_API_REPO_COMMITS = "https://api.github.com/repos/%s/%s/commits";

        private static Map<String, List<CommitDto>> getReposLastOneMount(final String userName, final String[] repoNames, final LocalDateTime since, final LocalDateTime until) {
            final Map<String, String> inputHeaders = new HashMap<>();
            inputHeaders.put(HEADER_GITHUB_API_ACCEPT.getFirst(), HEADER_GITHUB_API_ACCEPT.getSecond());
            inputHeaders.put(HEADER_GITHUB_API_VERSION.getFirst(), HEADER_GITHUB_API_VERSION.getSecond());

            final Map<String, List<CommitDto>> map = new HashMap<>(repoNames.length);

            for (String repoName : repoNames) {
                final String URI = GITHUB_API_REPO_COMMITS.formatted(userName, repoName);

                final Map<String, String> parameters = new HashMap<>();
                parameters.put(PARAM_PER_PAGE.getFirst(), String.valueOf(PARAM_PER_PAGE.getSecond()));
                parameters.put("since", since.format(DateTimeFormatter.ISO_DATE_TIME));
                parameters.put("until", until.format(DateTimeFormatter.ISO_DATE_TIME));

                Map<String, String> outputHeaders = new HashMap<>();
                outputHeaders.put(HEADER_OUT_LINK, null);

                final String jsonFirstPage = RequestUtil.getRequest(URI, parameters, inputHeaders, outputHeaders);
                final String headerLinkValue = outputHeaders.get(HEADER_OUT_LINK);
                System.err.println(jsonFirstPage);
                System.err.println(headerLinkValue);
                final List<CommitDto> commitDtoList = new ArrayList<>(JsonUtil.jsonToDto(jsonFirstPage, userName, repoName));


                if (HeaderLinkUtil.isPaging(headerLinkValue)) {
                    final int size = HeaderLinkUtil.lastPageNumber(headerLinkValue);

                    @SuppressWarnings("unchecked") CompletableFuture<String>[] completableFutures = new CompletableFuture[size - 1];
                    for (int i = 2; i <= size; i++) {
                        int temp = i;
                        completableFutures[i - 2] = CompletableFuture.supplyAsync(() -> getReposLastOneMountNext(userName, temp, repoName, since, until), THREAD_POOL_EXECUTOR);
                    }
                    CompletableFuture<Void> completableFuture = CompletableFuture.allOf(completableFutures).thenApply(_ -> {
                        for (int index = 0; index < size - 1; index++) {
                            String json = completableFutures[index].exceptionally(throwable -> {
                                log.error(throwable.getMessage(), throwable);
                                return "";
                            }).join();
                            commitDtoList.addAll(JsonUtil.jsonToDto(json, userName, repoName));
                        }
                        return null;
                    });
                    var _ = completableFuture.join();
                }
                map.put(repoName, commitDtoList);
            }
            return map;
        }

        private static String getReposLastOneMountNext(final String userName, final int page, final String repoName, final LocalDateTime since, final LocalDateTime until) {
            final String URI = GITHUB_API_REPO_COMMITS.formatted(userName, repoName);

            final Map<String, String> parameters = new HashMap<>();
            parameters.put(PARAM_PER_PAGE.getFirst(), String.valueOf(PARAM_PER_PAGE.getSecond()));
            parameters.put("since", since.format(DateTimeFormatter.ISO_DATE_TIME));
            parameters.put("until", until.format(DateTimeFormatter.ISO_DATE_TIME));
            parameters.put(PARAM_PAGE_KEY, String.valueOf(page));

            final Map<String, String> inputHeaders = new HashMap<>();
            inputHeaders.put(HEADER_GITHUB_API_ACCEPT.getFirst(), HEADER_GITHUB_API_ACCEPT.getSecond());
            inputHeaders.put(HEADER_GITHUB_API_VERSION.getFirst(), HEADER_GITHUB_API_VERSION.getSecond());

            return RequestUtil.getRequest(URI, parameters, inputHeaders, null);
        }


        public record CommitDto(String hash, LocalDateTime time, String author,
                                CommitDetailApi.CommitDetailDto CommitDetailDto) {
        }


        private static class JsonUtil {
            @SneakyThrows
            public static List<CommitDto> jsonToDto(final String json, final String userName, final String repoName) {
                final ArrayNode nodeRootArray = JacksonUtil.MAPPER.readTree(json).withArray("");

                final List<CommitDto> commitDtoList;
                if (!nodeRootArray.isEmpty()) {
                    commitDtoList = new ArrayList<>(nodeRootArray.size());
                    for (JsonNode nodeRootElement : nodeRootArray) {
                        final String hash = nodeRootElement.at("/sha").asText();
                        final JsonNode nodeCommit = nodeRootElement.at("/commit");
                        final JsonNode nodeAuthor = nodeCommit.at("/author");
                        final String author = nodeAuthor.at("/name").asText();
                        final LocalDateTime dateTime = LocalDateTime.parse(nodeAuthor.at("/date").asText(), DateTimeFormatter.ISO_DATE_TIME);
                        //final String message = nodeCommit.at("/message").asText();

                        CommitDetailApi.CommitDetailDto commitDetailDto = CommitDetailApi.getReposCommitDetail(userName, repoName, hash);
                        final var commitDto = new CommitDto(hash, dateTime, author, commitDetailDto);
                        commitDtoList.add(commitDto);
                    }

                } else commitDtoList = Collections.emptyList();
                return commitDtoList;
            }

        }

    }


    @Slf4j
    public static class ReposApi {
        private static final String GITHUB_API_USER_NAME_REPOS = "https://api.github.com/users/%s/repos";
        //private static final Pair<String, String> PARAM_DIRECTION = Pair.of("direction", "desc");


        private static String[] getUserRepos(String userName) {
            final String URI = GITHUB_API_USER_NAME_REPOS.formatted(userName);
            final Map<String, String> parameters = new HashMap<>();
            //parameters.put(PARAM_DIRECTION.getFirst(), PARAM_DIRECTION.getSecond());
            parameters.put(PARAM_PER_PAGE.getFirst(), String.valueOf(PARAM_PER_PAGE.getSecond()));
            //parameters.put(PARAM_PAGE_KEY, String.valueOf(1));

            final Map<String, String> inputHeaders = new HashMap<>();
            inputHeaders.put(HEADER_GITHUB_API_ACCEPT.getFirst(), HEADER_GITHUB_API_ACCEPT.getSecond());
            inputHeaders.put(HEADER_GITHUB_API_VERSION.getFirst(), HEADER_GITHUB_API_VERSION.getSecond());

            Map<String, String> outputHeaders = new HashMap<>();
            outputHeaders.put(HEADER_OUT_LINK, null);

            final String jsonFirstPage = RequestUtil.getRequest(URI, parameters, inputHeaders, outputHeaders);
            final String headerLinkValue = outputHeaders.get(HEADER_OUT_LINK);
            final List<String> repoNames = new ArrayList<>(ReposApi.findJsonRepoName(jsonFirstPage));

            if (HeaderLinkUtil.isPaging(headerLinkValue)) {
                final int size = HeaderLinkUtil.lastPageNumber(headerLinkValue);

                @SuppressWarnings("unchecked") CompletableFuture<String>[] completableFutures = new CompletableFuture[size - 1];

                for (int i = 2; i <= size; i++) {
                    int temp = i;
                    completableFutures[temp - 2] = CompletableFuture.supplyAsync(() -> getUserReposNextPage(userName, temp), THREAD_POOL_EXECUTOR);
                }
                CompletableFuture<Void> completableFuture = CompletableFuture.allOf(completableFutures).thenApply(_ -> {
                    for (int index = 0; index < size - 1; index++) {
                        String jsonPage = completableFutures[index].exceptionally(throwable -> {
                            log.error(throwable.getMessage(), throwable);
                            return "";
                        }).join();
                        repoNames.addAll(ReposApi.findJsonRepoName(jsonPage));

                    }
                    return null;
                });
                var _ = completableFuture.join();
            }

            return repoNames.toArray(String[]::new);
        }

        @SneakyThrows
        private static @NotNull List<String> findJsonRepoName(@NotNull String json) {
            final List<String> repoNames;
            if (json.isEmpty() || json.equals("[]")) return Collections.emptyList();

            final ArrayNode nodeRootArray = JacksonUtil.MAPPER.readTree(json).withArray("");
            if (nodeRootArray.isEmpty()) return Collections.emptyList();
            else repoNames = new ArrayList<>(nodeRootArray.size());

            for (JsonNode node : nodeRootArray) {
                final String repoName = node.at("/name").asText();
                repoNames.add(repoName);
            }

/*
            final String findFirst = "\"name\":\"";
            final String findEnd = "\"";

            int firstIndex = json.indexOf(findFirst);
            if (firstIndex == -1) throw new IllegalArgumentException();

            int endIndex = json.substring(firstIndex + findFirst.length(), firstIndex + 100 + findFirst.length()).indexOf(findEnd);
            if (endIndex == -1) throw new IllegalArgumentException();

            return json.substring(firstIndex + findFirst.length(), firstIndex + endIndex + findFirst.length());


 */
            return repoNames;
        }

        private static String getUserReposNextPage(String userName, int page) {
            final String URI = GITHUB_API_USER_NAME_REPOS.formatted(userName);
            final Map<String, String> parameters = new HashMap<>();
            //parameters.put(PARAM_DIRECTION.getFirst(), PARAM_DIRECTION.getSecond());
            parameters.put(PARAM_PER_PAGE.getFirst(), String.valueOf(PARAM_PER_PAGE.getSecond()));
            parameters.put(PARAM_PAGE_KEY, String.valueOf(page));

            final Map<String, String> inputHeaders = new HashMap<>();
            inputHeaders.put(HEADER_GITHUB_API_ACCEPT.getFirst(), HEADER_GITHUB_API_ACCEPT.getSecond());
            inputHeaders.put(HEADER_GITHUB_API_VERSION.getFirst(), HEADER_GITHUB_API_VERSION.getSecond());

            return RequestUtil.getRequest(URI, parameters, inputHeaders, null);
        }


    }


    private static class HeaderLinkUtil {
        private static boolean isPaging(String headerLink) {
            return headerLink != null && !headerLink.isEmpty();
        }

        private static int lastPageNumber(String headerLinkValue) {
            final int length = headerLinkValue.length();

            final int lastIndex = length - 13;
            int beginIndex = length - 13;
            int temp = 0;
            for (int i = 1; ; i++) {
                String strNumber = headerLinkValue.substring(beginIndex - i, lastIndex);
                try {
                    temp = Integer.parseInt(strNumber);
                } catch (NumberFormatException _) {
                    return temp;
                }
            }
        }
    }


}
