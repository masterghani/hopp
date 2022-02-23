import React, { Component } from 'react';
import { Auth } from 'aws-amplify';
import './login.css';
import { Router, Redirect } from 'react-router-dom';

class LoginForm extends Component {
    constructor(props) {
        super(props);

        this.state = {
            signedin: false,
            username: '',
            password: '',
            loginFailed: false,
            serverFailure: false,
        }
        
        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleSubmit(e) {
        e.preventDefault();
        const { username, password} = this.state;

        Auth.signIn({
            username: username,
            password: password,
        })
        .then((user) => {
            this.setState({signedin: true})
            sessionStorage.setItem('user', JSON.stringify(user.attributes))
            this.props.history.push('/RiderPost')
            // this.handleConfirmedSignin() // uncomment this and comment out above when deployed
        })
        .catch(err => {
            this.setState({loginFailed: true})
            console.log(err)
        });

    }

    async handleConfirmedSignin() {
        const user = await Auth.currentAuthenticatedUser()
        console.log(user)
        fetch('http://localhost:8080/posts', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(user.attributes),
        })
        .then(()=>{
            sessionStorage.setItem("user", JSON.stringify(user.attributes))
            this.setState({signedin: true})
            this.props.history.push("/RiderPost")
        })
        .catch(()=> {

            console.log('failed to register the new user\'s data on backend')
            this.setState({
                signedin: false,
                username: '',
                password: '',
                loginFailed: false,
                serverFailure: true,
            })
            
        })
        
     
    }

    handleChange(e) {
        this.setState({
            [e.target.name]: e.target.value
        });
    }

    render() {
        const {signedin}  = this.state;
    
        if (signedin) {
            return (
                <h1> You Have Signed In </h1>
            )
        } else {
            return (
                <form onSubmit={ this.handleSubmit } class="login-form"> 
                    <input type="text" placeholder="Email or Username" class="input2" name='username' value={ this.state.username} onChange={ this.handleChange } /><br />
                    <input type="password" placeholder="Password" class="input2" name='password' value={this.state.password} onChange={ this.handleChange } /><br />
                    <button class="btn">log in</button>
                    <span>I forgot my username or password</span><br />
                    { this.state.loginFailed ? <span className="loginAlarm">wrong username or password</span> : null }
                    { this.state.serverFailure ? <span className="loginAlarm">server failed please retry</span> : null }
                </form>
            )
        }
        
    }

}

export default LoginForm;