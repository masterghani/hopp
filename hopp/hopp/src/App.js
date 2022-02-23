import React, { Component, useState } from 'react';
import {
  BrowserRouter as Router,
  Switch,
  Route,
  Link,
  Redirect
} from "react-router-dom";
import logo from './logo.svg';
import './App.css';
import Amplify, { Auth } from 'aws-amplify';
import config from './aws-exports'
import '@aws-amplify/ui/dist/style.css';
import SignUpForm from './SignUpForm';
import LoginForm from './LoginForm';
import RiderPost from './RiderPost';
import DriverPost from './DriverPost';
import TopNav from './TopNav';
import './login.css';
import AccountPage from './AccountPage';

Amplify.configure(config);



class App extends Component{
  constructor(props) {
    super(props);

    this.state = {
      isLoginNav: true
    }
  }


  render() {
    return (
      <Router>
      <div className='body'>
      <Switch>
          <ProtectedRoute exact path="/login" component={LoginSignup}/>
          <ProtectedRoute exact path="/account" component={AccountPage}/>
          {/* <Route exact path="/notifications" component={notifications}/>
          <Route exact path="/upcoming-trips" component={UpcomingTrips}/> */}
          <ProtectedRoute exact path="/DriverPost" component={DriverPost}/>
          <ProtectedRoute exact path="/RiderPost" component={RiderPost}/>
          <ProtectedRoute path="/" component={notFound}/>
        </Switch>
      </div>
      </Router>
    );
  }

}

const notFound = () => {
  return (
    <h1> 404 Not Found </h1>
  )
}

/*
 The logic for protected routing.
 If the user has not logged in, any routing will be redirected to the login page
 If the user has logged in, any attempt to access the login page will be redirected to 
 the RiderPost homepage

*/
const ProtectedRoute = ({ path, component }) => {
  if (sessionStorage.getItem("user") === null) {
    return (
      path === '/login' ? <Route exact path={path} component={component}/> : <Redirect to='/login'/>
    )
  }
  if (path === '/login') {
    return (
      <Redirect to='/DriverPost'/>
    )
  }
  return (
    <Route exact path={path} component={component}/>
  )
}

const LoginSignup = ({ history }) => {
  const [isLoginPage, setToLogin] = useState(true)

  if (isLoginPage) {
    return (
      <div>
        <TopNav />
        <div className='container'>
          <div className='indicatorContainer'>
            <div className="notCurrentForm" onClick={() => setToLogin(false)}>Sign Up</div>
            <div className="currentForm" onClick={() => setToLogin(true)}>Log In</div>
          </div>
          <LoginForm history={ history } />
        </div>
      </div>
    )
  } else {
    return (
      <div>
        <TopNav />
        <div className='container'>
          <div className='indicatorContainer'>
            <div className="currentForm" onClick={() => setToLogin(false)}>Sign Up</div>
            <div className="notCurrentForm" onClick={() => setToLogin(true)}>Log In</div>
          </div>
          <SignUpForm history={ history } />
        </div>
      </div>
    )
  }
  
}

// export default withAuthenticator(App, true);
export default App;
