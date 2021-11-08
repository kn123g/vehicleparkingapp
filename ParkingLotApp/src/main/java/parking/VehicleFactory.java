package parking;

import java.util.Date;

public class VehicleFactory {

	Vehicle getVehicle(int type,int number,Date time,String slot){
		Vehicle obj = null;
		switch(type) {
		case 1:
			obj = new Car(number,time,slot);
			return obj;
		case 2:
			obj = new Bike(number,time,slot);
			return obj;
		case 3:
			obj = new Cycle(number,time,slot);
			return obj;
		default :
			return obj;
		}
	}
	
	
}
