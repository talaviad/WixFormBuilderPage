import React, { Component } from 'react';
import SaveForm from './SaveForm';
import { Jumbotron, Button , PageHeader, Label, FormGroup, FormControl, ControlLabel } from 'react-bootstrap';
import '../css/NewFormPage.css'


var inp = [];

class NewFormPage extends Component {
          constructor(props) 
          {
                super(props)
                this.state = {
                    currentid: 1,
                    fieldLabel: '',
                    InputName: '',
                    inputs: [],
                    formSavedName: 'enter the form name here',
                    saveIsClicked: false
                }
                this.changeFieldLabel = this.changeFieldLabel.bind(this);
                this.changeInputName = this.changeInputName.bind(this);
                this.addLabelToForm = this.addLabelToForm.bind(this);
                this.render = this.render.bind(this);
          }

          changeFieldLabel(e) {
                this.setState({fieldLabel: e.target.value, InputName:this.state.InputName, inputs:this.state.inputs})
          }

          changeInputName(e) {
                this.setState({fieldLabel:this.state.fieldLabel, InputName: e.target.value, inputs:this.state.inputs});
          }

          addLabelToForm(param,inputOb,fieldOb,e) {
                var input = inputOb.value;
                var field = fieldOb.value;
                if (input==='' || field ==='') {
                    this.setState({fieldLabel: '', InputName:'', inputs:this.state.inputs }) 
                    return
                }
                this.setState({fieldLabel: '', InputName:'', inputs: this.state.inputs.concat([{fieldLabel: field,inputname: input,inputtype:param.value, id: this.state.currentid }])}) 
                inp.push({fieldLabel: field,inputname: input,inputtype:param.value, id: this.state.currentid})
                inputOb.value = '';
                fieldOb.value = '';
                this.setState({currentid:this.state.currentid+1}) 
          }

          render() {
            return (
              <div>
                    <PageHeader id="pageheader">
                      Welcome To A New Form Page  <br /><br />
                    </PageHeader>  
                    <div id ="bigLeftDiv" >
                        <div id ="leftDiv"  >
                              <p> <strong>We Are Going Now To Add A New Form,
                              Please Fiil The Following Form And Press 'Add Field' To Add It. Do This Proccess 
                              As Much As You Want And Finally, Press The 'Save And Add The Form' Button </strong></p> <br />
                              <form id="myform">
                                    <h3 id="h31" > <Label>field label</Label> <input id="input1" type="text" value ={this.state.fieldLabel} onChange={this.changeFieldLabel}/> </h3> 
                                    <h3> <Label>input name</Label> <input id="input2" type="text" value={this.state.InputName} onChange={this.changeInputName}/> </h3> 
                                    <h3 id="h32" > <Label>type</Label> <span> </span>
                                        <select id="select" name="input types" > 
                                            <option value="text">Text</option>
                                            <option value="color">Color</option>
                                            <option value="date">Date</option>
                                            <option value="email">Email</option>
                                            <option value="tel">Tel</option>
                                            <option value="number">Number</option>
                                        </select>
                                    </h3>
                              </form>
                            <br />
                            <Button id="button" bsStyle="primary" bsSize="large" onClick={()=>this.addLabelToForm(document.getElementById("select"),document.getElementById("input2"),document.getElementById("input1"))} >Add A Label </Button>
                                    <br /> <br />
                      </div>
                      <div id="rightDiv" > <SaveForm inputs={this.state.inputs}/> </div>
                  </div>
                  <div id="bigRightDiv" > 
                      <Jumbotron id="jumbotron" >
                            <h2 id="h21" > Your Currently Updated Form </h2> <br />
                            <FormGroup controlId="formBasicText">
                                {this.state.inputs.map((input)=>  
                                <div> 
                                      <ControlLabel class="controllabel" >{input.inputname}: </ControlLabel> 
                                      <FormControl class="formcontrol" 
                                          type={input.inputtype}
                                          placeholder={input.fieldLabel}
                                          />
                              </div>
                                )} 
                            </FormGroup>
                      </Jumbotron>
                  </div>
            </div>
            ); 
          }
}
export default NewFormPage;
