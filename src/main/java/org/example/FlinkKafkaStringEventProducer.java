package org.example;

import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.common.serialization.TypeInformationSerializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer011;
import org.example.entity.Event;

import java.util.Random;
import java.util.UUID;

public class FlinkKafkaStringEventProducer {

    private static final String[] ID1S = new String[]{
            "A","B","C","d","E","F","G","H"
    };

    private static final String[] ID2S = new String[]{
            "甲","乙","丙","丁","戊","己","庚","辛"
    };

    private static final String[] ID3S = new String[]{
            "1","2","3","4","5","6","7","8"
    };

    public static final Random random = new Random();

    public static void main(String[] args) {
        final ParameterTool params = ParameterTool.fromArgs(args);
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.getConfig().setGlobalJobParameters(params);

        env.addSource(new SourceFunction<String>() {

            @Override
            public void run(SourceContext<String> sourceContext) throws Exception {
                while (true){
                    for (int i=0;i<=100;i++){
                        sourceContext.collect(ID1S[random.nextInt(ID1S.length)]);
                        //sourceContext.collect(Event.builder().id1(ID1S[random.nextInt(ID1S.length)]).id2(ID2S[random.nextInt(ID2S.length)]).id3(ID3S[random.nextInt(ID3S.length)]).value(UUID.randomUUID().toString()).build());
                    }
                    Thread.sleep(10000);
                }
            }

            @Override
            public void cancel() {

            }
        }).addSink(new FlinkKafkaProducer011<String>(
                "192.168.18.101:9092",
                "event_test",
                new SimpleStringSchema()
        ));
                //.addSink(new PrintSinkFunction<Event>());
        try {
            env.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}