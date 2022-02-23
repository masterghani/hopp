import React, { Component } from 'react';
import { Auth } from 'aws-amplify';


class AccountPage extends Component {
    constructor(props) {
        super(props);

        this.state = {
            signedin: true,

        }

    }

    render() {
        return (
            <h1> Account Page </h1>
        )    
    }

}

export default AccountPage;