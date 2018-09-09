import React, { Component } from 'react';
import { Link } from 'react-router-dom'
import FormsTable from './FormsTable'
import { Jumbotron, Grid, Button, Row, Col, Image } from 'react-bootstrap';


class ListFormsPage extends Component {
  render() {
    return (
      <div>
            <Grid>
                  <Row >
                        <Col>
                            <Jumbotron >
                                  <Image src="./forms.jpg" style={{width:'100%', height:'250px',}} rounded  /> 
                                  <h4 style={{color: 'blue',textAlign: 'center',}}> <font size="6"> Welcome To List Forms Page </font></h4>
                            </Jumbotron>
                        </Col>
                  </Row>
                  <FormsTable /> <br />                 
          </Grid>
          <div style={{top:'50%' ,left: '45%',position:'relative'}}> <Link to='./NewFormPage'> <Button bsStyle="primary" bsSize="large">Add A New Form </Button> </Link> </div>
    </div>
      
    );
  }
}
export default ListFormsPage;
