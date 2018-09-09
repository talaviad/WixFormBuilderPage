import React, { Component } from 'react';
import { Route, Switch} from 'react-router-dom'
import ListFormsPage from './ListFormsPage'
import NewFormPage from './NewFormPage'
import SubmitPage from './SubmitPage'
import SubmitionsPage from './SubmitionsPage'


class Pages extends Component {
  render() {
    return (
      <div>
            <Switch>
                <Route exact path='/' component={ListFormsPage} />
                <Route path='/ListFormsPage' component={ListFormsPage} />
                <Route path='/NewFormPage' component={NewFormPage} />
                <Route path='/SubmitPage/:number' component={SubmitPage} />
                <Route path='/SubmitionsPage/:number' component={SubmitionsPage} /> 
            </Switch>
     </div>
    );
  }
}
export default Pages;

