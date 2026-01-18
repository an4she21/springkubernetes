import React, { Component } from 'react';
import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom';
import Voiture from './components/Voiture';
import VoitureListe from './components/VoitureListe';
import { Navbar, Nav, Container } from 'react-bootstrap';

class App extends Component {
  render() {
    return (
      <Router>
        <div>
          <Navbar bg="dark" variant="dark" expand="lg">
            <Container>
              <Navbar.Brand>Gestion de Voitures</Navbar.Brand>
              <Navbar.Toggle aria-controls="basic-navbar-nav" />
              <Navbar.Collapse id="basic-navbar-nav">
                <Nav className="me-auto">
                  <Nav.Link as={Link} to="/">Ajouter Voiture</Nav.Link>
                  <Nav.Link as={Link} to="/liste">Liste des Voitures</Nav.Link>
                </Nav>
              </Navbar.Collapse>
            </Container>
          </Navbar>
          <Container className="mt-4">
            <Routes>
              <Route path="/" element={<Voiture />} />
              <Route path="/liste" element={<VoitureListe />} />
            </Routes>
          </Container>
        </div>
      </Router>
    );
  }
}

export default App;

