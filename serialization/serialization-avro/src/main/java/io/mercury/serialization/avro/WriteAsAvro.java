package io.mercury.serialization.avro;

import java.io.File;

import org.apache.avro.file.DataFileWriter;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.specific.SpecificDatumWriter;

import io.mercury.serialization.avro.msg.AvroTextMsg;

public class WriteAsAvro {

    public static void main(String[] args) throws Exception {

        AvroTextMsg ual1 = new AvroTextMsg();

        ual1.setContent("login");
        ual1.setGender(1);
        ual1.setIpAddress("192.168.1.1");
        ual1.setProvience("henan");
        ual1.setUserName("lily");

        UserActionLog ual2 = UserActionLog.newBuilder().setActionType("logout").setGender(0).setIpAddress("192.168.1.2")
                .setProvience("hebie").setUserName("jim").build();

        // 把两条记录写入到文件中（序列化）
        // 把两条记录写入到文件中（序列化）
        // DatumWriter 是一个 接口，需要用它的实现类创建对象
        // 如果创建了实例化对象，则使用  SpecificDatumWriter  来创建对象
        // 如果没有创建实例化对象，则使用 GenericDatumWriter 来创建对象
        DatumWriter<UserActionLog> writer = new SpecificDatumWriter<UserActionLog>();
        DataFileWriter<UserActionLog> fileWriter = new DataFileWriter<UserActionLog>(writer);

        // 创建序列化文件,文件会创建到项目的根目录下
        fileWriter.create(UserActionLog.getClassSchema(), new File("userlogaction.avro"));

        // 写入内容
        fileWriter.append(ual1);
        fileWriter.append(ual2);

        fileWriter.flush();
        fileWriter.close();

    }
	
	
	
}
