package org.eclipselabs.occ.marsrobot.remote.host;

import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;

import org.eclipselabs.occ.marsrobot.remote.WorldService;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;

import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Reference;

@Component(
	immediate = true, 
	properties = { 
		"service.exported.interfaces=*",
		"service.exported.configs=ecf.generic.server" 
})
public class WorldComponent implements WorldService {

	ConfigurationAdmin configurationAdmin;

	@Reference
	public void setConfigurationAdmin(ConfigurationAdmin configurationAdmin) {
		this.configurationAdmin = configurationAdmin;
	}

	@Override
	public void createWall(int x, int y, int z, int length, int height,
			int rotate) {
		System.out.println("Service creates new wall using ConfigurationAdmin");
		try {
			Configuration configuration = getFactoryConfiguration();
			System.out.println("PID is: " + configuration.getPid());

			Dictionary<String, Integer> wallProperties = toDictionary(x, y, z,
					length, height, rotate);
			configuration.update(wallProperties);
			System.out.println("Service created new wall");
		} catch (IOException e) {
			System.out.println("Error on creating configuration for Wall.");
			e.printStackTrace();
		}
	}

	private Configuration getFactoryConfiguration() throws IOException {
		Configuration configuration = configurationAdmin
				.createFactoryConfiguration(
						"org.eclipselabs.occ.marsrobot.wall.WallComponent",
						null);
		return configuration;
	}

	private Dictionary<String, Integer> toDictionary(int x, int y, int z,
			int length, int height, int rotate) {
		Dictionary<String, Integer> wallProps = new Hashtable<String, Integer>();
		wallProps.put("x", x);
		wallProps.put("y", y);
		wallProps.put("z", z);
		wallProps.put("length", length);
		wallProps.put("height", height);
		wallProps.put("rotate", rotate);
		return wallProps;
	}

}
