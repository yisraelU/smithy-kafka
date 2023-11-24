package kafka;

import software.amazon.smithy.model.pattern.InvalidUriPatternException;
import software.amazon.smithy.model.pattern.SmithyPattern;
import software.amazon.smithy.utils.Pair;

import java.util.*;
import java.util.stream.Collectors;

public final class TopicPattern  extends SmithyPattern {

    private final Map<String, String> queryLiterals;

    private TopicPattern(Builder builder, Map<String, String> queryLiterals) {
        super(builder);
        this.queryLiterals = queryLiterals;
    }

    /**
     * Parse a URI pattern string into a TopicPattern.
     *
     * <p>The provided value must match the origin-form request-target
     * grammar production in RFC 7230, section 5.3.1.
     *
     * @param uri URI pattern to parse.
     * @return Returns the parsed URI pattern.
     * @throws InvalidUriPatternException for invalid URI patterns.
     * @see <a href="https://tools.ietf.org/html/rfc7230#section-5.3.1">RFC 7230 Section 5.3.1</a>
     */
    public static software.amazon.smithy.model.pattern.TopicPattern parse(String uri) {
        if (uri.endsWith("?")) {
            throw new InvalidUriPatternException("URI patterns must not end with '?'. Found " + uri);
        } else if (!uri.startsWith("/")) {
            throw new InvalidUriPatternException("URI pattern must start with '/'. Found " + uri);
        } else if (uri.contains("#")) {
            throw new InvalidUriPatternException("URI pattern must not contain a fragment. Found " + uri);
        }

        String[] parts = uri.split(java.util.regex.Pattern.quote("?"), 2);
        String[] unparsedSegments = parts[0].split(java.util.regex.Pattern.quote("/"));
        List<Segment> segments = new ArrayList<>();
        // Skip the first "/" segment, and thus assume offset of 1.
        int offset = 1;
        for (int i = 1; i < unparsedSegments.length; i++) {
            String segment = unparsedSegments[i];
            segments.add(Segment.parse(segment, offset));
            // Add one to account for `/`
            offset += segment.length() + 1;
        }

        Map<String, String> queryLiterals = new LinkedHashMap<>();
        // Parse the query literals outside of the general pattern
        if (parts.length == 2) {
            if (parts[1].contains("{") || parts[1].contains("}")) {
                throw new InvalidUriPatternException("URI labels must not appear in the query string. Found " + uri);
            }
            for (String kvp : parts[1].split(java.util.regex.Pattern.quote("&"))) {
                String[] parameterParts = kvp.split("=", 2);
                String actualKey = parameterParts[0];
                if (queryLiterals.containsKey(actualKey)) {
                    throw new InvalidUriPatternException("Literal query parameters must not be repeated: " + uri);
                }
                queryLiterals.put(actualKey, parameterParts.length == 2 ? parameterParts[1] : "");
            }
        }

        return new software.amazon.smithy.model.pattern.TopicPattern(builder().pattern(uri).segments(segments), queryLiterals);
    }

    /**
     * Get an immutable map of query string literal key-value pairs.
     *
     * @return An immutable map of parsed query string literals.
     */
    public Map<String, String> getQueryLiterals() {
        return Collections.unmodifiableMap(queryLiterals);
    }

    /**
     * Gets a specific query string literal parameter value.
     *
     * @param parameter Case-sensitive name of the parameter to retrieve.
     * @return Returns the optionally found parameter value.
     */
    public Optional<String> getQueryLiteralValue(String parameter) {
        return Optional.ofNullable(queryLiterals.get(parameter));
    }

    /**
     * Determines if the pattern conflicts with another pattern.
     *
     * @param otherPattern SmithyPattern to check against.
     * @return Returns true if there is a conflict.
     */
    public boolean conflictsWith(software.amazon.smithy.model.pattern.TopicPattern otherPattern) {
        if (!getConflictingLabelSegmentsMap(otherPattern).isEmpty()) {
            return true;
        }

        List<Segment> segments = getSegments();
        List<Segment> otherSegments = otherPattern.getSegments();

        // By now we know there are no label conflicts, so one uri has more
        // segments than the other then they don't conflict.
        if (segments.size() != otherSegments.size()) {
            return false;
        }

        // Now we need to check for the differences  in the static segments of the uri.
        for (int i = 0; i < segments.size(); i++) {
            Segment segment = segments.get(i);
            Segment otherSegment = otherSegments.get(i);
            // We've already checked for label conflicts, so we can skip them here.
            if (segment.isLabel() || otherSegment.isLabel()) {
                continue;
            }
            if (!segment.getContent().equals(otherSegment.getContent())) {
                return false;
            }
        }

        // At this point, the path portions are equivalent. If the query
        // string literals are the same, then the patterns conflict.
        return queryLiterals.equals(otherPattern.queryLiterals);
    }

    @Deprecated
    public List<Pair<Segment, Segment>> getConflictingLabelSegments(software.amazon.smithy.model.pattern.TopicPattern otherPattern) {
        Map<Segment, Segment> conflictingSegments = getConflictingLabelSegmentsMap(otherPattern);
        return conflictingSegments.entrySet().stream()
                .map(entry -> Pair.of(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof software.amazon.smithy.model.pattern.TopicPattern)) {
            return false;
        }
        software.amazon.smithy.model.pattern.TopicPattern otherPattern = (software.amazon.smithy.model.pattern.TopicPattern) other;
        return super.equals(other) && queryLiterals.equals(otherPattern.queryLiterals);
    }

    @Override
    public int hashCode() {
        return super.hashCode() + queryLiterals.hashCode();
    }

}

