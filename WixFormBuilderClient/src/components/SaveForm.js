import React, { Component } from 'react';
import { Redirect } from 'react-router-dom'
import {  Button } from 'react-bootstrap';


class SaveForm extends Component {
        constructor(props) {
            super(props)
            this.state = {
                formSavedName: 'New Form Name',
                redirectOrNot: false
            }
            this.changeSavedInput = this.changeSavedInput.bind(this);
            this.saveAndAddTheForm = this.saveAndAddTheForm.bind(this);
        }

        changeSavedInput(formName) {
            this.setState({formSavedName: formName.value, redirectOrNot:this.redirectOrNot})
        }

        saveAndAddTheForm() {
            if (this.props.inputs.length !== 0 && this.state.formSavedName !== 'New Form Name') {
                this.setState({formSavedName: this.state.formSavedName, redirectOrNot:true})
                let formName = this.state.formSavedName;
                var oReq = new XMLHttpRequest();
                oReq.open("POST", "http://localhost:8080/WixFormBuilderServer//FormBuilder?forms=newform&formname="+formName); 
                oReq.send(JSON.stringify(this.props.inputs));
            }  
        }

        toRedirect() {
            if (this.state.redirectOrNot) {
                return <Redirect to='./ListFormsPage'> </Redirect>
            }
        }

        render() {
            return (
                <div>
                    <input style={{margin:'25px'}} id="enter-form-name" type="text" placeholder= {this.state.formSavedName} onChange={()=>this.changeSavedInput(document.getElementById("enter-form-name"))} />
                    <Button style={{color:'white', backgroundColor:'red'}} bsStyle="primary" bsSize="large" onClick={this.saveAndAddTheForm}>Save And Add The Form</Button>
                    {this.toRedirect()}
                </div>
            );
        }
  }
  export default SaveForm;



