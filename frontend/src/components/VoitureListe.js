import React, { Component } from 'react';
import { Card, Table, Button } from 'react-bootstrap';
import axios from 'axios';
import MyToast from './MyToast';
import API_BASE_URL from '../config/api';

class VoitureListe extends Component {
  state = {
    voitures: [],
    show: false
  };

  componentDidMount() {
    this.findAllVoitures();
  }

  findAllVoitures = () => {
    axios.get(`${API_BASE_URL}/voitures`)
      .then(response => {
        this.setState({ voitures: response.data });
      })
      .catch(error => {
        console.error("Error loading voitures:", error);
      });
  };

  deleteVoiture = (voitureId) => {
    axios.delete(`${API_BASE_URL}/voitures/${voitureId}`)
      .then(response => {
        if (response.data != null) {
          this.setState({ show: true });
          setTimeout(() => this.setState({ show: false }), 3000);
          this.setState({
            voitures: this.state.voitures.filter(voiture => voiture.id !== voitureId)
          });
        }
      })
      .catch(error => {
        console.error("Error deleting voiture:", error);
        this.setState({ show: false });
      });
  };

  render() {
    return (
      <div>
        <div style={{ display: this.state.show ? "block" : "none" }}>
          <MyToast children={{ show: this.state.show, message: "Voiture supprimée avec succès.", type: "danger" }} />
        </div>

        <Card className="border border-dark bg-dark text-white">
          <Card.Header>
            <h3>Liste des Voitures</h3>
          </Card.Header>
          <Card.Body>
            <Table striped bordered hover variant="dark">
              <thead>
                <tr>
                  <th>Id</th>
                  <th>Marque</th>
                  <th>Modele</th>
                  <th>Couleur</th>
                  <th>Immatricule</th>
                  <th>Année</th>
                  <th>Prix</th>
                  <th>Propriétaire</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {this.state.voitures.length === 0 ? (
                  <tr align="center">
                    <td colSpan="9">Aucune voiture disponible</td>
                  </tr>
                ) : (
                  this.state.voitures.map((voiture) => (
                    <tr key={voiture.id}>
                      <td>{voiture.id}</td>
                      <td>{voiture.marque}</td>
                      <td>{voiture.modele}</td>
                      <td>{voiture.couleur}</td>
                      <td>{voiture.immatricule}</td>
                      <td>{voiture.annee}</td>
                      <td>{voiture.prix}</td>
                      <td>
                        {voiture.proprietaire ? 
                          `${voiture.proprietaire.nom} ${voiture.proprietaire.prenom}` : 
                          'N/A'}
                      </td>
                      <td>
                        <Button
                          size="sm"
                          variant="outline-danger"
                          onClick={this.deleteVoiture.bind(this, voiture.id)}
                        >
                          Supprimer
                        </Button>
                      </td>
                    </tr>
                  ))
                )}
              </tbody>
            </Table>
          </Card.Body>
        </Card>
      </div>
    );
  }
}

export default VoitureListe;

