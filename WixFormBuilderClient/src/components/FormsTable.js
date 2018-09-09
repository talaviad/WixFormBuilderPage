import React, { Component } from 'react';
import { Link } from 'react-router-dom';
import ReactBasicTable from 'react-basic-table';


class FormsTable extends Component {

    constructor(props) {
        super(props);
        this.state = {forms: [] };
      }

    componentWillMount(){
        var oReq = new XMLHttpRequest();
        var self = this;
        oReq.onreadystatechange = function() {
            if (this.readyState === 4 && this.status === 200) {
               var myJsonString = JSON.parse(oReq.responseText);
               self.setState({ forms: myJsonString });
            }
        };
        oReq.open("GET", "http://localhost:8080/WixFormBuilderServer//FormBuilder?forms=mainpage"); 
        oReq.send();
    }

    render() {
      var columns = ['FormId', 'FormName', 'Submmisions', 'SubmitPage', 'SubmissionsPage'];
      var rows = [];
      for(let obj of this.state.forms) {
        let newRow =[
            <span>{obj['FormId']}</span>,
            <span>{obj['FormName']}</span>,
            <span>{obj['Submmisions']}</span>,
            <Link to={"./SubmitPage/"+obj['FormId'] }>view </Link>,
            <Link to={"./SubmitionsPage/"+obj['FormId'] }>view </Link>
            ];
        rows.push(newRow);
      }
      return (
          <div>
              <ReactBasicTable rows={rows} columns={columns} />
         </div>
      )
    }
}
export default FormsTable;