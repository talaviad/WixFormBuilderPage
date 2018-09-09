import React from 'react';
import { BrowserRouter } from 'react-router-dom'
import ReactDOM from 'react-dom';
import './index.css';
import registerServiceWorker from './registerServiceWorker';
import Pages from './components/Pages';


ReactDOM.render(<BrowserRouter>
                <Pages />
                </BrowserRouter> , document.getElementById('root'));
registerServiceWorker();
