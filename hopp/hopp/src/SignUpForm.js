import React, { Component } from 'react';
import { Auth } from 'aws-amplify';
import './login.css';

class SignUpForm extends Component {
    constructor(props) {
        super(props);

        this.state = {
            onConfirmPage: false,
            username: '',
            birthday: '',
            password: '',
            address: '',
            gender: '',
            phone: '',
            name: '',
            email: '',
            confirmationCode: '',
        }
        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleSubmit(e) {
        e.preventDefault();
        const {onConfirmPage, username, password, email, phone, birthday, address, gender, name, confirmationCode} = this.state;

        if (!onConfirmPage) {
            Auth.signUp({
                username: username,
                password: password,
                attributes: {
                    email: email,
                    phone_number: phone,
                    gender: gender,
                    birthdate: birthday,
                    address: address,
                    given_name: name.split(' ')[0],
                    family_name: name.split(' ')[1]
                }
            })
            .then(() => {
                console.log('signed up');
                this.setState({
                onConfirmPage: true
                })
            })
            .catch(err => console.log(err));

        } else {
            Auth.confirmSignUp(username, confirmationCode)
            .then(() => {
                console.log('confirmed sign up')
                this.handleConfirmedSignup()
            })
            .catch(err => console.log(err));
        }
    }

    async handleConfirmedSignup() {
        const user = await Auth.currentAuthenticatedUser()
        console.log(user)
        fetch('http://localhost:8080/posts', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(user.attributes),
        })
        .then(()=>{
            sessionStorage.setItem("user", JSON.stringify(user.attributes))
            this.props.history.push("/RiderPost")
        })
        .catch(()=> {

            console.log('failed to register the new user\'s data on backend')
            this.setState({
                onConfirmPage: false,
                username: '',
                birthday: '',
                password: '',
                address: '',
                gender: '',
                phone: '',
                name: '',
                email: '',
                confirmationCode: '',
            })
            
        })
        
     
    }

    handleChange(e) {
        this.setState({
            [e.target.name]: e.target.value
        });
    }

    render() {
        const {onConfirmPage}  = this.state;
    
        if (onConfirmPage) {
            return (
                <form onSubmit={ this.handleSubmit } class="signup-form">
                    <input type="text" placeholder="Comfirmation Code" value={this.state.confirmationCode} className="input" name='confirmationCode' onChange={ this.handleChange } /><br />
                    <button className="btn">Confirm</button>
                </form>
            )
        } else {
            return (
                <form onSubmit={ this.handleSubmit } class="signup-form"> 
                    <input type="text" placeholder="Your Full Name" value={this.state.name} class="input" name='name' onChange={ this.handleChange } /><br />
                    <input type="text" placeholder="Gender" class="input" value={this.state.gender} name='gender' onChange={ this.handleChange } /><br />
                    <input type="date" placeholder="Birthday" class="input" value={this.state.birthday} name='birthday' onChange={ this.handleChange } /><br />
                    <input type="text" placeholder="Your Email Address" value={this.state.email} class="input" name='email' onChange={ this.handleChange } /><br />
                    <input type="text" placeholder="Your Address" class="input" value={this.state.address} name='address' onChange={ this.handleChange } /><br />
                    <input type="text" placeholder="Your Phone Number" class="input" value={this.state.phone} name='phone' onChange={ this.handleChange } /><br />
                    <input type="text" placeholder="Choose a Username" class="input" value={this.state.username} name='username' onChange={ this.handleChange } /><br />
                    <input type="password" placeholder="Choose a Password" class="input" value={this.state.password} name='password' onChange={ this.handleChange } /><br />
                    <button className="btn">Create account</button>
                </form>
            )
        }
        
    }

}

export default SignUpForm;