package kafka;

import software.amazon.smithy.model.node.Node;
import software.amazon.smithy.model.shapes.ShapeId;
import software.amazon.smithy.model.traits.AbstractTrait;
import software.amazon.smithy.model.traits.AbstractTraitBuilder;
import software.amazon.smithy.model.traits.Trait;
import software.amazon.smithy.utils.ToSmithyBuilder;

import java.util.Objects;

/**
 * Defines the Kafka mailbox to which a message is sent.
 */
public class KafkaEndpointTrait extends AbstractTrait implements ToSmithyBuilder<KafkaEndpointTrait> {
    public static final ShapeId ID = ShapeId.from("karafka.api#kafkaEndpoint");
    private final TopicPattern topic;

    private KafkaEndpointTrait(KafkaEndpointTrait.Builder builder) {
        super(ID, builder.sourceLocation);
        topic = Objects.requireNonNull(builder.topic, "topic not set");
    }

    public static final class Provider extends AbstractTrait.Provider {
        public Provider() {
            super(ID);
        }

        @Override
        public Trait createTrait(ShapeId target, Node value) {
            KafkaEndpointTrait.Builder builder = builder().sourceLocation(value);
            value.expectObjectNode()
                    .expectStringMember("topic", s -> builder.topic(TopicPattern.parse(s)))
            KafkaEndpointTrait result = builder.build();
            result.setNodeCache(value);
            return result;
        }
    }

    public TopicPattern getTopic() {
        return topic;
    }

    @Override
    protected Node createNode() {
        return Node.objectNodeBuilder()
                .sourceLocation(getSourceLocation())
                .withMember("topic", Node.from(topic))
                .build();
    }

    /**
     * @return Returns a builder used to create an Http trait.
     */
    public static KafkaEndpointTrait.Builder builder() {
        return new KafkaEndpointTrait.Builder();
    }

    @Override
    public KafkaEndpointTrait.Builder toBuilder() {
        return new KafkaEndpointTrait.Builder().sourceLocation(getSourceLocation()).topic(topic);
    }

    // Avoid inconsequential equality differences based on defaulting code to 200.
    @Override
    public boolean equals(Object other) {
        if (!(other instanceof KafkaEndpointTrait)) {
            return false;
        } else if (other == this) {
            return true;
        } else {
            KafkaEndpointTrait trait = (KafkaEndpointTrait) other;
            return topic.equals(trait.topic);
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(toShapeId(), topic);
    }

    /**
     * Builder used to create an Http trait.
     */
    public static final class Builder extends AbstractTraitBuilder<KafkaEndpointTrait, KafkaEndpointTrait.Builder> {
        private TopicPattern topic;

        public KafkaEndpointTrait.Builder topic(TopicPattern topic) {
            this.topic = topic;
            return this;
        }

        @Override
        public KafkaEndpointTrait build() {
            return new KafkaEndpointTrait(this);
        }
    }
}
