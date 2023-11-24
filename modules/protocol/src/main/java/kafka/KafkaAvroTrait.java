package kafka;

import software.amazon.smithy.model.node.Node;
import software.amazon.smithy.model.node.ObjectNode;
import software.amazon.smithy.model.shapes.ShapeId;
import software.amazon.smithy.model.traits.AnnotationTrait;
public class KafkaAvroTrait extends AnnotationTrait {
    public static final ShapeId ID = ShapeId.from("kafka#kafkaAvro");

    public KafkaAvroTrait(ObjectNode node) {
        super(ID, node);
    }

    public KafkaAvroTrait() {
        this(Node.objectNode());
    }

    public static final class Provider extends AnnotationTrait.Provider<KafkaAvroTrait> {
        public Provider() {
            super(ID, KafkaAvroTrait::new);
        }
    }
}
