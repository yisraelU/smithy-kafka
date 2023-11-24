package kafka;

import software.amazon.smithy.model.SourceException;
import software.amazon.smithy.model.SourceLocation;
import software.amazon.smithy.model.shapes.ShapeId;
import software.amazon.smithy.model.traits.StringTrait;

public class MessageHeaderTrait extends StringTrait {
    public static final ShapeId ID = ShapeId.from("kafka.api#messageHeader");

    public MessageHeaderTrait(String value, SourceLocation sourceLocation) {
        super(ID, value, sourceLocation);

        if (getValue().isEmpty()) {
            throw new SourceException("messageHeader field name binding must not be empty", getSourceLocation());
        }
    }

    public MessageHeaderTrait(String value) {
        this(value, SourceLocation.NONE);
    }

    public static final class Provider extends StringTrait.Provider<MessageHeaderTrait> {
        public Provider() {
            super(ID, MessageHeaderTrait::new);
        }
    }
}
