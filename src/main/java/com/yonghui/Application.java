package com.yonghui;

import com.yonghui.address.AddressContext;
import com.yonghui.address.AddressSplit;
import com.yonghui.address.dto.DetailAddress;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.*;

/**
 * @author jasonbiao
 * @date 2020-08-06 14:40
 * description: <p>
 *
 * </p>
 */
@SpringBootApplication
@Slf4j
public class Application implements ApplicationRunner {

    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }


    @Autowired
    private AddressSplit addressSplit;

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {

        FileInputStream fileInputStream = new FileInputStream(new File("/Users/xubiao/project/ofs/address-split/src/main/resources/test.txt"));
        BufferedReader br = new BufferedReader(new InputStreamReader(fileInputStream));

        FileOutputStream outputStream = new FileOutputStream(new File("/Users/xubiao/project/ofs/address-split/src/main/resources/result.txt"));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outputStream));

        String line = null;
        while((line = br.readLine()) != null) {
            String[] lines = line.split("\\$\\$");

            if(lines.length != 2) {
                continue;
            }
            AddressContext context = new AddressContext();
            context.setFirstAddress(lines[0]);
            context.setSecondAddress(lines[1]);
            DetailAddress detailAddress = addressSplit.process(context);

//            bw.write(line);
//            bw.newLine();
            bw.write(String.format("%s  %s", context.getFirstAddress(), context.getSecondAddress()));
            bw.newLine();
            bw.write(String.format("%s  %s", detailAddress.getFirstAddress(), detailAddress.getSecondAddress()));
            bw.newLine();
            bw.newLine();
            bw.flush();
        }

        fileInputStream.close();
        outputStream.close();

    }
}
