import {useHistory} from 'react-router-dom';
import './Welcome.css';
export default function Welcome(){
    const history = useHistory();
    const checkInHandle = ()=>{
        history.push("/checkin")
    }
    const checkOutHandle = ()=>{
        history.push("/checkout")
    }
    const findVehicle = ()=>{
        history.push("/findvehicle")
    }
    const showLots = ()=>{
        history.push("/showLots")
    }
    return(
        <div className="content">
        <button className="btn" onClick={checkInHandle}>CHECK IN</button>
        <button className="btn" onClick={checkOutHandle}>CHECK OUT</button>
        <button className="btn" onClick={findVehicle}>FIND VEHICLE</button>
        <button className="btn show-lots" onClick={showLots}>SHOW LOTS</button>
        </div>
    )
}