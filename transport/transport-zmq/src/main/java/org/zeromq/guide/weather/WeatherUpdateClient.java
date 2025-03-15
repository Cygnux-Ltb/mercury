package org.zeromq.guide.weather;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

import java.util.StringTokenizer;

//
//  Weather update client in Java
//  Connects SUB socket to tcp://localhost:5556
//  Collects weather updates and finds avg temp in zipcode
//
public class WeatherUpdateClient {
	
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		try (ZContext context = new ZContext()) {
			// Socket to talk to server
			System.out.println("Collecting updates from weather server");
			ZMQ.Socket subscriber = context.createSocket(SocketType.SUB);
			subscriber.connect("tcp://localhost:5556");

			// Subscribe to zipcode, default is NYC, 10001
			String filter = (args.length > 0) ? args[0] : "10001 ";
			subscriber.subscribe(filter.getBytes(ZMQ.CHARSET));

			// Process 100 updates
			int update_nbr;
			long total_temp = 0;
			for (update_nbr = 0; update_nbr < 100; update_nbr++) {
				// Use trim to remove the tailing '0' character
				String string = subscriber.recvStr(0).trim();

				StringTokenizer sscanf = new StringTokenizer(string, " ");
				int zipcode = Integer.parseInt(sscanf.nextToken());
				int temperature = Integer.parseInt(sscanf.nextToken());
				int relhumidity = Integer.parseInt(sscanf.nextToken());

				total_temp += temperature;
			}

			System.out.println(String.format("Average temperature for zipcode '%s' was %d.", filter,
					(int) (total_temp / update_nbr)));
		}
	}
}
