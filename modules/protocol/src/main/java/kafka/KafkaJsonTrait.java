package kafka;

import software.amazon.smithy.model.node.Node;
import software.amazon.smithy.model.node.ObjectNode;
import software.amazon.smithy.model.shapes.ShapeId;
import software.amazon.smithy.model.traits.AnnotationTrait;
public class KafkaJsonTrait extends AnnotationTrait {
    public static final ShapeId ID = ShapeId.from("kafka#kafkaJson");

    public KafkaJsonTrait(ObjectNode node) {
        super(ID, node);
    }

    public KafkaJsonTrait() {
        this(Node.objectNode());
    }

    public static final class Provider extends AnnotationTrait.Provider<KafkaJsonTrait> {
        public Provider() {
            super(ID, KafkaJsonTrait::new);
        }
    }
}
