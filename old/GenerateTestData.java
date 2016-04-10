import java.io.BufferedWriter;
import java.io.FileWriter;
import java.sql.Timestamp;
import java.util.Random;

public class GenerateTestData {
	public static void main(String args[]) {
		try(BufferedWriter bw = new BufferedWriter(new FileWriter("../data.sql"));) {
			//friendships
			for(int i = 0; i < 200; i++) {
				// allows for people to be friends with themselves
				String friend1 = (int)(Math.random() * 99) + "";
				String friend2 = (int)(Math.random() * 99) + "";
				String status = (new Random()).nextInt(2) + "";
				Timestamp established = null;
				if(Integer.parseInt(status) == 1) {
					long offset = Timestamp.valueOf("2014-01-01 00:00:00").getTime();
					long end = Timestamp.valueOf("2015-01-01 00:00:00").getTime();
					long diff = end - offset + 1;
					established = new Timestamp(offset + (long)(Math.random() * diff)); 
				}
				if(established == null) {
					bw.write("INSERT INTO FRIENDS VALUES(" + "\'" + friend1 + "\'" + ", " + "\'" + friend2 + "\'" + ", " + "\'" 
				+ status + "\'" + ", "  + established + ");");
				}
				else {
					bw.write("INSERT INTO Friends VALUES(" + "\'" + friend1 + "\'" + ", " + "\'" + friend2 + "\'" + ", " + "\'" 
				+ status + "\'" + ", "  + "\'" + established + "\'" + ");");
				}
				
				bw.newLine();
			}
			// messages
			for(int i = 0; i < 300; i++) {
				// allows for people to be friends with themselves
				String msgId = i + "";
				String subject = "subject " + i;
				String senderId = (new Random()).nextInt(100) + "";
				int type = (new Random()).nextInt(2);
				String recipId = null;
				if(type == 0) {
					recipId = (new Random()).nextInt(100) + "";
				}
				else {
					recipId = (new Random()).nextInt(10) + "";
				}
				long offset = Timestamp.valueOf("2014-01-01 00:00:00").getTime();
				long end = Timestamp.valueOf("2015-01-01 00:00:00").getTime();
				long diff = end - offset + 1;
				Timestamp timeSent = new Timestamp(offset + (long)(Math.random() * diff)); 
				String msgTxt = "message " + i + " text";
				
				bw.write("INSERT INTO Messages VALUES(" + "\'" + msgId + "\'" + ", " + "\'" + subject + "\'" + ", " + "\'" +
				senderId + "\'" + ", " + "\'" + recipId + "\'" + ", " + "\'" + timeSent + "\'" + ", " + "\'" +
				msgTxt + "\'" + ", " + "\'" + (type+1) + "\'" + ");");
				bw.newLine();
			}
			
			bw.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		
	}
}