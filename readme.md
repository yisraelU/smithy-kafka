# Smithy4s-Kafka

### Protocol
 - Contains the following Smithy [traits](https://smithy.io/2.0/spec/model.html#traits) for Kafka
   - KafkaAvro
     - provides a protocol for kafka with serialization through avro
   - KafkaJson
       - provides a protocol for kafka with serialization through json
   - Endpoint
     - applies to an operation with the following details
         - Topic : specify name of topic applies to operation , affects consumer and producer
         - dynamic topics , TopicLabel for pulling value from structure to use as part or hole of topic
       - Key
         - a trait that specifies a field to be used for the key of the message
       - Headers
         - a trait that specifies a field to be used for the headers of the message, there can be multiple headers
       - Partition
           - a trait that specifies a field to be used for the designated partition 
       - Timestamp
           - a trait that specifies a field to be used for the timestamp of the message