import React, { Component } from 'react';
import { Redirect } from 'react-router-dom'
import { Jumbotron, Button , FormGroup, FormControl, ControlLabel } from 'react-bootstrap';


class SubmitPage extends Component {
    
    constructor(props)
        {
            super(props);
            this.showForm = this.showForm.bind(this);
            this.submmitingTheForm = this.submmitingTheForm.bind(this);
            this.userNameInputChanged = this.userNameInputChanged.bind(this);
            this.showForm = this.showForm.bind(this);
            this.labelChanged = this.labelChanged.bind(this);
            this.checkIfFormExist = this.checkIfFormExist.bind(this);
            this.state = {userNameInput:'',submitted:false, currentFormInputs: undefined,formName:undefined};
            this.FormExpectedId = parseInt(this.props.match.params.number, 10);
            this.FormExist = false; 
            //this.formName = undefined; 
        }  

        getFormName() {
            var oReq = new XMLHttpRequest();
            var self = this;
            oReq.onreadystatechange = function() {
                if (this.readyState === 4 && this.status === 200) {
                    self.setState({formName: oReq.responseText});
                }
            };
            oReq.open("GET", "http://localhost:8080/WixFormBuilderServer//FormBuilder?forms=getformname&formnumber="+this.FormExpectedId); 
            oReq.send();
        } 

        checkIfFormExist(formId) {
            var oReq = new XMLHttpRequest();
            var found = false;
            var self = this;
            oReq.onreadystatechange = function() {
                if (this.readyState === 4 && this.status === 200) {
                    if (oReq.responseText !== "false") {
                        found = true;
                        self.setState({currentFormInputs: JSON.parse(oReq.responseText)});
                    }
                }
            };
            oReq.open("GET", "http://localhost:8080/WixFormBuilderServer//FormBuilder?forms=findform&formnumber="+formId,false); 
            oReq.send();
            return found;
        }

        userNameInputChanged(e){
            this.setState({userNameInput:e.target.value,submitted:this.state.submitted,inputs:this.state.inputs})
        }

        labelChanged(inputId,elementId) {
            this.state.currentFormInputs[inputId]['fieldlabel'] = elementId.value;
            this.setState({currentFormInputs:this.state.currentFormInputs});
        }

        submmitingTheForm(){
            if (this.state.userNameInput !== '') {
                this.setState({submitted:true})
            }
            var oReq = new XMLHttpRequest();
            oReq.open("POST", "http://localhost:8080/WixFormBuilderServer//FormBuilder?forms=submitform&formnumber="+this.FormExpectedId+"&username="+this.state.userNameInput); 
            oReq.send(JSON.stringify(this.state.currentFormInputs));
        }

        showForm() {
            if (this.state.submitted===true) {
                return <Redirect to='../ListFormsPage'> </Redirect>
            }
            if (!this.FormExist) {
                return <div>Sorry, but the form you are looking for does not exist</div>
            }
            return (<div>
                <div style={{float: 'left', width:'33%', height:'100%'}}>  </div>
                <div style={{float: 'right', width:'67%', height:'100%'}}>  
                        <div style={{float: 'left', width:'60%', height:'100%'}}> 
                                <Jumbotron id="jumbotron" >
                                        <h2 id="h21" > '{this.state.formName}' form </h2> <br />
                                        <FormGroup controlId="formBasicText">
                                            {this.showInputs()}
                                        </FormGroup>
                                </Jumbotron>
                                <div id="leftOne" style={{height:'100%', width:'50%', float:'left'}}>   
                                        <h2 style={{marginTop:'5px'}}>   <input style={{width:'100%', height:'100%'}} id="user-name" type="text" placeholder="your name" value={this.state.userNameInput} onChange={this.userNameInputChanged} />    </h2>      
                                </div> 
                                <div id="righttOne" style={{height:'100%', width:'50%', float:'right'}}> 
                                        <Button style={{width:'100%',height:'100%'}} bsStyle="primary" bsSize="large" id="user-name" onClick={()=> this.submmitingTheForm()} > Submit The Form </Button>
                                </div>
                        </div>
                </div>
                </div>)
        }

        showInputs() {
            var arr = [];
            for(let input of this.state.currentFormInputs) {
                let item = 
                <div> 
                    <ControlLabel style={{margin:'0px 0px 0px 190px'}} class="controllabel" >{input['inputname']}: </ControlLabel> 
                    <FormControl id = {input['id']} class="formcontrol" style={{margin:'0px 0px 0px 190px'}}
                        type={input['inputtype']}
                        value={input['fieldlabel']}
                        onChange={()=>this.labelChanged(parseInt(input['id'],10)-1,document.getElementById(input['id']))}
                    />
                </div> 
                arr.push(item); 
            }
            return arr;
         }

         componentWillMount(){
            this.FormExist = this.checkIfFormExist(this.FormExpectedId);     
            if (this.FormExist)
                this.getFormName(); 
         }

        render() {
            return (
                <div>
                    <h1 style={{textAlign:'center', color:'blue'}}> <strong> Submmiting Form Page </strong> </h1>
                    <div> {this.showForm()}</div> 
                </div>);
        }
  }
  export default SubmitPage;