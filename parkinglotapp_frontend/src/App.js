import {BrowserRouter as Router,Route,Switch,Redirect} from 'react-router-dom';
import Header from './shared/components/Header';
import Home from './components/Home/Home';
import Welcome from './components/Welcome/Welcome';
import CheckIn from './components/CheckIn/CheckIn';
import CheckOut from './components/CheckOut/CheckOut';
import FindVehicle from './components/FindVehicle/FindVehicle';
import ShowLots from './components/ShowLots/ShowLots';
import './App.css';


function App() {
  return (
    <Router>
      <main>
      <Header/>
      <Switch>
        <Route path="/home" exact>
          <Home/>
        </Route>
        <Route path="/welcome" exact>
          <Welcome/>
        </Route>
        <Route path="/CheckIn" exact>
          <CheckIn/>
        </Route>
        <Route path="/CheckOut" exact>
          <CheckOut/>
        </Route>
        <Route path="/FindVehicle" exact>
          <FindVehicle/>
        </Route>
        <Route path="/ShowLots" exact>
          <ShowLots/>
        </Route>
        <Redirect to="/home"/>
       </Switch>
      </main>
    </Router>
  );
}

export default App;
