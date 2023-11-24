package kafka;

import software.amazon.smithy.model.node.Node;
import software.amazon.smithy.model.node.ObjectNode;
import software.amazon.smithy.model.shapes.ShapeId;
import software.amazon.smithy.model.traits.AnnotationTrait;

public class MessageKeyTrait extends AnnotationTrait {
    public static final ShapeId ID = ShapeId.from("coalmine.kafka#messageKey");

    public MessageKeyTrait(ObjectNode node) {
        super(ID, node);
    }

    public MessageKeyTrait() {
        this(Node.objectNode());
    }

    public static final class Provider extends AnnotationTrait.Provider<MessageKeyTrait> {
        public Provider() {
            super(ID, MessageKeyTrait::new);
        }
    }
}
