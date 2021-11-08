import './ShowLots.css';
import {showLots} from '../../services/LotsService';
import {useEffect,useState} from 'react';

export default function ShowLots(){
    const showLotsArr = [
        {floor:'A',capacity:10,noofvehicle:5,availability:5},
        {floor:'B',capacity:10,noofvehicle:6,availability:4},
        {floor:'C',capacity:10,noofvehicle:3,availability:7},
        {floor:'D',capacity:10,noofvehicle:10,availability:'-'}
    ];
    let [showLotsState,setshowLotsState] = useState({message:"",status:false});
    let [lots,setLots] = useState([]);
    useEffect(()=>{
        showLots()
        .then((result)=>{
            setLots([...result.message]);
        })
        .catch((result)=>{
            setshowLotsState({message:result.message,status:result.status});
        });
    },[])
    const headers = ['FLOOR','CAPACITY','NO OF VEHICLE','AVAILABILITY'];
    return (
        <div className="show-lots">
        <h3>Total number of floors 4</h3>
        {lots.length > 0 ? <table>
            <thead>
                <tr>{headers.map((header,i) => <th key={i+'th'}>{header}</th>)}</tr>
            </thead>
            <tbody>
                {lots.map((element,i)=>{
                    let floor = Object.keys(element)[0];
                    let capacity = 10;
                    let noofvehicles = element[Object.keys(element)[0]];
                    let availability = +capacity - +noofvehicles;
                    
                    return <tr key={i+"tr"}><th>{floor}</th><th>{capacity}</th><th>{noofvehicles}</th><th>{availability}</th></tr>
                })}
            </tbody>
        </table> : 
         <div className="lots-error">
         <h4>{showLotsState.message}</h4>
         </div>
        }
        </div>
    );
}