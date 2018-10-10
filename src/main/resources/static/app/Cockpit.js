import React from 'react';
import ReactDOM from 'react-dom';



const refreshReact = () => {
    ReactDOM.render(
        <Cockpit/>,
        document.getElementById('cockpit')
    );
};

class Cockpit extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
            <h1>I'm live.</h1>
        );
    }
}

refreshReact();

