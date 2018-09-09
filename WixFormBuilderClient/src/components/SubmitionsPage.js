import React, { Component } from 'react';
import ReactBasicTable from 'react-basic-table';


class SubmitionsPage extends Component {
        constructor(props)
        {
            super(props);
            this.FormExist = false;
            this.showSummitersForm = this.showSummitersForm.bind(this);
            this.state = {userNameInput:'',submitted:false,inputs:[]}
            this.FormExpectedId = parseInt(this.props.match.params.number, 10);
            this.checkIfFormExist = this.checkIfFormExist.bind(this);
            this.setState({formSubmmiters:undefined, formName: undefined});
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
            var self = this;
            var found = false;
            oReq.onreadystatechange = function() {
                if (this.readyState === 4 && this.status === 200) {
                    if (oReq.responseText !== "false" && JSON.parse(oReq.responseText).length!==0) {
                        found = true;
                        self.setState({formSubmmiters: JSON.parse(oReq.responseText)});
                    }
                }
            };
            oReq.open("GET", "http://localhost:8080/WixFormBuilderServer//FormBuilder?forms=getsubmmiters&formnumber="+this.FormExpectedId,false); 
            oReq.send();
            return found;
        }

        showSummitersForm () {
            if (!this.FormExist) {
                return <div>
                            <h3 style={{textAlign:'center'}}> Sorry, but the submissions form you are looking for does not exist or there are not any forms submmited yet </h3>
                    </div>
            }
            var numOfLabels = (Object.keys(this.state.formSubmmiters[0]).length-1)/2;
            var columns = ['Username'];
            for(let i=1; i<=numOfLabels; i++) {
                columns=columns.concat([this.state.formSubmmiters[0]['inputname'+i]])
            }
            var rows = [];
            for(let obj of this.state.formSubmmiters) {
                let newRow =[ <span>{obj['username']}</span> ];
                for (let i=1; i<=numOfLabels; i++) {
                    newRow.push(<span>{obj['fieldlabel'+i]}</span>)     
                }
                rows.push(newRow);
            }
            return <div>
                        <div>  
                            <h3 style={{textAlign:'center', color:'grey'}}> '{this.state.formName}' submmitions page </h3>
                        </div>
                        <div>
                            <ReactBasicTable rows={rows} columns={columns} />
                        </div>
                </div> 
        }

        componentWillMount(){
            this.FormExist = this.checkIfFormExist(this.FormExpectedId); 
            if (this.FormExist)
                this.getFormName(); 
        }

        render() {
            return (
                <div>
                    <h1 style={{textAlign:'center'}}> Submmitions Form Page </h1>
                    <div> {this.showSummitersForm()}</div> 
                </div>);
        }   
}
export default SubmitionsPage;


                    