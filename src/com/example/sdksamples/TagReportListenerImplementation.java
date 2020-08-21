package com.example.sdksamples;

import com.impinj.octane.ImpinjReader;

import com.impinj.octane.Tag;
import com.impinj.octane.TagReport;
import com.impinj.octane.TagReportListener;
import java.io.BufferedReader;
import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Calendar;

import java.util.List;

public class TagReportListenerImplementation implements TagReportListener {

    @Override
    public void onTagReported(ImpinjReader reader, TagReport report) {
        List<Tag> tags = report.getTags();

        for (Tag t : tags) {
        	String epc = t.getEpc().toString();
            System.out.print(" EPC: " + epc);

            
            try {
                Date date = Calendar.getInstance().getTime();
                long timeMilli = date.getTime();
                
                
                epc = epc.replaceAll(" ", "");
             

                URL url = new URL("http://localhost:8000/data/" + epc + "/" + timeMilli);
                URLConnection con = url.openConnection();
                HttpURLConnection http = (HttpURLConnection)con;
                http.setRequestMethod("POST"); // PUT is another valid option
                http.setDoOutput(true);
                
                byte[] out = "{\"userid\":\"?\",\"timemilli\":\"?\"}" .getBytes(StandardCharsets.UTF_8);
                int length = out.length;

                http.setFixedLengthStreamingMode(length);
                http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                http.connect();
                try(OutputStream os = http.getOutputStream()) {
                    os.write(out);
                }


                BufferedReader br = new BufferedReader(new InputStreamReader(
                    (con.getInputStream())));
                String output;
                System.out.println("Output from Server .... \n");
                while ((output = br.readLine()) != null) {
                    System.out.println(output);
                }
              } catch (MalformedURLException e) {
                e.printStackTrace();
              } catch (IOException e) {
                e.printStackTrace();
              }

            if (reader.getName() != null) {
                System.out.print(" Reader_name: " + reader.getName());
            } else {
                System.out.print(" Reader_ip: " + reader.getAddress());
            }

            if (t.isAntennaPortNumberPresent()) {
                System.out.print(" antenna: " + t.getAntennaPortNumber());
            }

            if (t.isFirstSeenTimePresent()) {
                System.out.print(" first: " + t.getFirstSeenTime().ToString());
            }

            if (t.isLastSeenTimePresent()) {
                System.out.print(" last: " + t.getLastSeenTime().ToString());
            }

            if (t.isSeenCountPresent()) {
                System.out.print(" count: " + t.getTagSeenCount());
            }

            if (t.isRfDopplerFrequencyPresent()) {
                System.out.print(" doppler: " + t.getRfDopplerFrequency());
            }

            if (t.isPeakRssiInDbmPresent()) {
                System.out.print(" peak_rssi: " + t.getPeakRssiInDbm());
            }

            if (t.isChannelInMhzPresent()) {
                System.out.print(" chan_MHz: " + t.getChannelInMhz());
            }

            if (t.isRfPhaseAnglePresent()) {
                System.out.print(" phase angle: " + t.getPhaseAngleInRadians());
            }

            if (t.isFastIdPresent()) {
                System.out.print("\n     fast_id: " + t.getTid().toHexString());

                System.out.print(" model: " +
                        t.getModelDetails().getModelName());

                System.out.print(" epcsize: " +
                        t.getModelDetails().getEpcSizeBits());

                System.out.print(" usermemsize: " +
                        t.getModelDetails().getUserMemorySizeBits());
            }

            System.out.println("");
        }
        
    }
}