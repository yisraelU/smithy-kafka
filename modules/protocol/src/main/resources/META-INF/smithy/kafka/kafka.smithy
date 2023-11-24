$version: "2.0"


metadata suppressions = [
    {
        id: "UnreferencedShape",
        namespace: "kafka",
        reason: "This is a library namespace."
    }
]

namespace kafka

@trait(selector: "service")
@protocolDefinition(traits: [topicLabel, messageKey, messageHeader])
@mixin
structure kafka {}

@trait(selector: "service")
@protocolDefinition()
structure kafkaAvro with [kafka] {}

@trait(selector: "service")
@protocolDefinition(traits:[jsonName])
structure kafkaJson with [kafka] {}

@trait(selector: "member[trait|required] :test(> :test(string, byte, short, integer, long, boolean, timestamp))")
structure topicLabel {}

@trait(selector: "member[trait|required] :test(> :test(string, byte, short, integer, long, boolean, timestamp))")
structure messageKey {}

@trait(selector: "structure >:test(member > :test(boolean, byte, short, integer, long, blob, string, timestamp")
structure messageHeader {}