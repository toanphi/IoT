package com.uet.iot.business.impl;

import com.uet.iot.BO.LircConfigBase;
import com.uet.iot.base.BaseResponse;
import com.uet.iot.business.ManageLircConfigBusiness;
import com.uet.iot.database.entity.SupportedDevice;
import com.uet.iot.database.repo.SupportedDeviceRepo;
import com.uet.iot.util.LircConfigCreator;
import com.uet.iot.util.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class ManageLircConfigBusinessImpl implements ManageLircConfigBusiness {
    private LircConfigBase lircConfigBase;

    @Autowired
    private SupportedDeviceRepo supportedDeviceRepo;

    @Override
    public ResponseEntity<Void> initRecord(String deviceName) {
        lircConfigBase = new LircConfigBase();
        lircConfigBase.setDeviceName(deviceName);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<BaseResponse> record(String btnName) throws IOException {
        LircConfigCreator lircConfigCreator = new LircConfigCreator();

        if (!lircConfigCreator.startProcess()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        String result = refactor(lircConfigCreator.recordLirc());
        System.out.println("refactored");

        System.out.println(result);

        if (result == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        lircConfigBase.getMap().put(btnName, result);

        return new ResponseEntity<>(new BaseResponse(Message.SUCCESS), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> finishRecord() throws IOException {
        if (!writeFileAndMove().contains("ready") || lircConfigBase.getMap().isEmpty()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        supportedDeviceRepo.save(new SupportedDevice(lircConfigBase.getDeviceName()));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public String writeFileAndMove() throws IOException {
        File file = new File(lircConfigBase.getDeviceName()+".lircd.conf");

        file.createNewFile();

        FileWriter fw = new FileWriter(file);
        String config = createLircConfigFile();
        fw.write(config);
        fw.close();

        execCommand("sudo mv " + file.getAbsolutePath() + " /etc/lirc/lircd.conf.d/" + lircConfigBase.getDeviceName()+".lircd.conf");
        execCommand("rm " + file.getAbsolutePath());
        execCommand("sudo /etc/init.d/lircd restart");
        return execCommand("sudo /etc/init.d/lircd status");
    }

    public String createLircConfigFile(){
        StringBuilder sb = new StringBuilder();
        sb.append("begin remote\n");
        sb.append("  name    " + lircConfigBase.getDeviceName() + "\n");
        sb.append("  flags RAW_CODES\n");
        sb.append("  eps            25\n");
        sb.append("  aeps          100\n");
        sb.append("\n");
        sb.append("  ptrail          0\n");
        sb.append("  repeat     0     0\n");
        sb.append("  gap    20921\n");
        sb.append("\n\n");
        sb.append("  begin raw_codes\n");
        lircConfigBase.getMap().entrySet().forEach(entry -> {
            sb.append("    name " + entry.getKey() + "\n");
            sb.append(entry.getValue() + " \n");
        });
        sb.append("  end raw_codes\n");
        sb.append("end remote");
        return sb.toString();
    }

    public String refactor(String in) {
        String out = in.replaceAll("\\s{2,}", ",");
        String[] arr = out.split(",");

        if (arr.length < 8){
            return null;
        }

        int count = 0;
        StringBuilder sb = new StringBuilder();
        int startDigit = 0;
        for (int i = 0 ; i < arr.length; i++){
            String temp = arr[i];
            if (isNumber(temp) && temp.length() <=4){
                startDigit = i;
                break;
            }
        }
        for (int i = startDigit ; i < arr.length; i++){
            String res = "";
            String temp = arr[i];
            if (isNumber(temp) && temp.length() <= 4){
                if (temp.length() == 3) {
                    res += "      " + temp;
                } else {
                    res += "     " + temp;
                }
                count++;
                if (count == 6){
                    res += "\n";
                    count = 0;
                }
            } else {
                break;
            }
            sb.append(res);
        }
        return sb.toString();
    }
    public boolean isNumber(String in){
        Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");

        if (in == null){
            return false;
        }
        return pattern.matcher(in).matches();
    }

    public String execCommand(String command) throws IOException {
        List<String> commands = new ArrayList<String>();
        if (System.getProperty("os.name").toLowerCase().startsWith("windows")){
            commands.add("cmd.exe");
        } else {
            commands.add("bash");
        }

        commands.add("-c");
        commands.add(command);

        String[] arr = new String[commands.size()];

        for (int i = 0 ; i < commands.size(); i++) {
            arr[i] = commands.get(i);
        }

        Runtime r = Runtime.getRuntime();
        StringBuilder sb = new StringBuilder();

        try {
            Process p = r.exec(arr);

            p.waitFor();
            BufferedReader b = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = "";

            while ((line = b.readLine()) != null) {
                sb.append(line);
            }

            b.close();
        } catch (Exception e) {
            System.err.println("Failed to execute bash with command: " + command);
            e.printStackTrace();
        }
        return sb.toString();
    }
}
