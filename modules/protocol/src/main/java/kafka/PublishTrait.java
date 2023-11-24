package kafka;


import software.amazon.smithy.model.SourceLocation;
import software.amazon.smithy.model.shapes.ShapeId;
import software.amazon.smithy.model.traits.StringTrait;

/**
 * {@code coalmine.kafka#publish} trait.
 */
public final class PublishTrait extends StringTrait {
    public static final ShapeId ID = ShapeId.from("coalmine.kafka#publish");
    private final Topic topic;

    public PublishTrait(String topic, SourceLocation sourceLocation) {
        super(ID, topic, sourceLocation);
        this.topic = Topic.parse(topic);
    }

    public PublishTrait(String topic) {
        this(topic, SourceLocation.NONE);
    }

    public static final class Provider extends StringTrait.Provider<PublishTrait> {
        public Provider() {
            super(ID, PublishTrait::new);
        }
    }

    /**
     * Gets the parsed topic of the trait.
     *
     * @return Returns the topic.
     */
    public Topic getTopic() {
        return topic;
    }
}