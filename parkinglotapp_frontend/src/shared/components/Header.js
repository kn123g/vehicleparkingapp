import {useLocation,useHistory} from 'react-router-dom';
import {useEffect,useState} from 'react';
import './Header.css';
export default function Header(){
    const location = useLocation();
    const history = useHistory();
    const [matchMessage] = useState([
        {path:'/home',message:'WELCOME TO PARKING'},
        {path:'/CheckIn',message:'CHECK IN'},
        {path:'/CheckOut',message:'CHECK OUT'},
        {path:'/FindVehicle',message:'FIND MY VEHICLE'},
        {path:'/ShowLots',message:'SHOW LOTS'}
    ]);
    let [message,setMessage] = useState('');
    let [HomeVisibility,setHomeVisibility] = useState(false);
    useEffect(() => {
        console.log(location.pathname); // result: '/secondpage'
        console.log(location.search); // result: '?query=abc'
        
        for(var i=0;i<matchMessage.length;i++){
            if(matchMessage[i].path === location.pathname){
                setMessage(matchMessage[i].message);
                break;
            }
        }
        setHomeVisibilityState();
        if(i>matchMessage.length-1){
            setMessage('');
        }
     }, [location,matchMessage]);

    const setHomeVisibilityState = ()=>{
        if(location.pathname !== '/home' && location.pathname !== '/welcome'){
            setHomeVisibility(true);
        }
        else{
            setHomeVisibility(false);
        }
     }

    return (
        <div className="head">
            <div className="title-container">
            {HomeVisibility ? <button className="btn" id="home-btn" onClick={()=>history.push('/home')}>Home</button> : null}
            <h1 id="title">PARKING LOT</h1>
            </div>
            <h3>
               {message}
            </h3>
        </div>
    )
}