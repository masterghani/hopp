import React, { Component } from 'react';
import { Link, Redirect } from "react-router-dom";
import './login.css';
import thumb from "./thumb.png";
import account from "./account.svg";

class TopNav extends Component {
    constructor(props) {
        super(props);

        this.state = {
            signedin: false,
            username: '',
            password: '',
        }
    }

    logoutHandler() {
        sessionStorage.clear();
        this.props.history.push('/login')
    }

    render() {
        
        return (
            
            <div className="topnav">
                <div className="topnav-left">
                    <img src={thumb} alt="thumb" className = "thumb" />
                    { sessionStorage.getItem('user') != null ? <h2 className="logout" onClick={this.logoutHandler.bind(this)}> Log Out </h2> : null}
                </div>
                <div className="topnav-right">
                    { sessionStorage.getItem('user') != null ? 
                    <div>
                        <Link to="/upcoming-trips">Upcoming Trips</Link> 
                        <Link to="/notifications">Notifications</Link>
                        <Link to="/account">
                            <img src={account} alt="account" className = "account"/>
                        </Link>
                    </div>
                    : null}
                </div>
            </div> 
        )
    }     

}

export default TopNav;