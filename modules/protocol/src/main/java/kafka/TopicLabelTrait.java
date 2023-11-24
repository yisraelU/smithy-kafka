package kafka;

import software.amazon.smithy.model.node.Node;
import software.amazon.smithy.model.node.ObjectNode;
import software.amazon.smithy.model.shapes.ShapeId;
import software.amazon.smithy.model.traits.AnnotationTrait;

/**
 * Binds a member to a Kafka label using the member name.
 */
public final class TopicLabelTrait extends AnnotationTrait {
    public static final ShapeId ID = ShapeId.from("coalmine.kafka#topicLabel");

    public TopicLabelTrait(ObjectNode node) {
        super(ID, node);
    }

    public TopicLabelTrait() {
        this(Node.objectNode());
    }

    public static final class Provider extends AnnotationTrait.Provider<TopicLabelTrait> {
        public Provider() {
            super(ID, TopicLabelTrait::new);
        }
    }
}