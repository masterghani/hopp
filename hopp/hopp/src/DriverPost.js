import React, { Component } from 'react';
import { Auth } from 'aws-amplify';
import './driver.css'
import TopNav from './TopNav';
import './login.css';
// import MapWrapped from './MapWrapped';
import { GoogleMapReact, Marker } from 'google-map-react';
import {Loader, LoaderOptions} from 'google-maps';


class DriverPost extends Component {
    constructor(props) {
        super(props);

        this.state = {
            signedin: true,
            markerLoc: {
                lat: 42.3601,
                lng: -71.0589
              },
            lastLoc: {
                lat: 42.3601,
                lng: -71.0589
            },
            zoom: 11,
        }
        // this.marker = React.createRef();

    }
    static defaultProps = {
        center: {
          lat: 42.3601,
          lng: -71.0589
        },
        zoom: 11
      };

    componentDidMount() {
        const apiKey = 'AIzaSyB4wKxvY-md1NKI3l2IODJ7LquYkK-D6zs'
        const loader = new Loader(apiKey);
 
        loader.load().then(google => {
            const map = new google.maps.Map(document.getElementById('map'), {
                center: this.props.center,
                zoom: 8,
            });
            const marker = new google.maps.Marker({
                position: this.props.center,
                map: map,
                draggable: true,
                title: 'MyPosition',
            });
            const directionsService = new google.maps.DirectionsService();
            const directionsRenderer = new google.maps.DirectionsRenderer();
            directionsRenderer.setMap(map);
            this.setState({
                map: map,
                marker: marker,
                dirService: directionsService,
                dirRenderer: directionsRenderer,
            })
            marker.addListener('dragend', ()=>{
                const newLoc = {
                        lat: marker.getPosition().lat(),
                        lng: marker.getPosition().lng(),
                }

                this.findPath(this.state.lastLoc, newLoc)
                this.setState({
                    lastLoc: newLoc
                })      
            })

        
        });

    }
    
    findPath(start, end) {
        const request = {
            origin:start,
            destination:end,
            travelMode: 'DRIVING'
          };
          this.state.dirService.route(request, (response, status) => {
            if (status === 'OK') {
                console.log(response)
              this.state.dirRenderer.setDirections(response);
            }
          });
    }
    

    render() {
        return (
            <div>
            <TopNav history={this.props.history}/>
            <div class = "left" >
                <div class="wrapper">
                     <div class="container">
                    <div class="questionnaire-title">Offer a Ride!</div>
                <div class="signup-form"> 
                    <input type= "text" placeholder= "Trip Name" class= "input" required />
                    <input  type= "text" placeholder= "Origin" class= "input" required />
                    <input  type= "text" placeholder= "Destination" class= "input" required />
                    <input type="datetime-local" placeholder="Date & Time" class="input" required /><br/>
                    <input type="number" placeholder="Price/Rider" class="input" min ="0" required /><br/>
                    <input type="number" placeholder="Seats Available" class="input" min ="1" required /><br/>
                    <button type="submit" class="btn">Post To Trip Database</button>
                </div>
            </div>
            </div>
            </div>
            
            <div id='map' style={{ height: '100vh', width: '100%', position: 'relative' }}></div>
            </div>
        )    
    }

}

export default DriverPost;

