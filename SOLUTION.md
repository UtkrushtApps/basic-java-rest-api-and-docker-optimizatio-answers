# Solution Steps

1. Refactor the Java code: Replace the permanent ArrayList of completed tasks with a simple completed task count to eliminate the memory leak.

2. Use a ConcurrentHashMap for ongoing (incomplete) tasks for thread safety and better concurrency.

3. Implement an ExecutorService in TaskService to handle background (async) statistics updates. Trigger stats recalculation on changes, but avoid blocking REST API calls (fire-and-forget). Debounce heavy updates.

4. Make the /tasks/stats endpoint return quickly using the last cached data, rather than recalculating synchronously.

5. Ensure shutdown of ExecutorService on application shutdown (via PreDestroy).

6. Revise the Dockerfile: Use the 'eclipse-temurin:17-jre-alpine' (slim) base image instead of a larger one.

7. Copy only the built JAR into the Docker image (not the source directory) to reduce context size and rebuild time.

8. Add efficient JVM memory flags (MaxRAMPercentage, UseContainerSupport) in Dockerfile's JAVA_OPTS.

9. Create a .dockerignore file to exclude everything except the built JAR from the build context, further speeding up builds.

10. Double-check that the app builds to a single JAR with all dependencies (Maven Spring Boot plugin, packaging=jar).

11. Test: Confirm API endpoints work, stats remain consistent with concurrent usage, memory does not grow with completed tasks, and Docker images are small and fast to build.

