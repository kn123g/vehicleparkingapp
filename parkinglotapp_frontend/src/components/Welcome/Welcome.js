import {useHistory} from 'react-router-dom';
import './Welcome.css';
export default function Welcome(){
    const history = useHistory();
    const checkInHandle = ()=>{
        history.push("/CheckIn")
    }
    const checkOutHandle = ()=>{
        history.push("/CheckOut")
    }
    const findVehicle = ()=>{
        history.push("/FindVehicle")
    }
    const showLots = ()=>{
        history.push("/ShowLots")
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