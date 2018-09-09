import React, { Component } from 'react';
import logo from './logo.svg';
import './App.css';
import { Link, Route, Switch} from 'react-router-dom'
import AnotherPage from './anotherPage'


//for dubegging purpose
class App extends Component {
  render() {
    return (
      <div>
      <div className="App">
        <header className="App-header">
          <img src={logo} className="App-logo" alt="logo" />
          <h1 className="App-title">Welcome to React</h1>
        </header>
        <p className="App-intro">
          To get started, edit <code>src/App.js</code> and save to reload.
        </p>
      </div>
      <div>
        <nav>
          <ul>
            <li> <Link to='/anotherPage'>Going To Another Page</Link> </li>
            <li> <Link to='/ListFormsPage'>Going To List Forms Page</Link> </li>
            </ul>
          </nav>
      </div> 
      </div>
    );
  }
}

export default App;