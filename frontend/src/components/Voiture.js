import React, { Component } from 'react';
import { Card, Form, Button, FormGroup, FormLabel, FormControl, Row, Col } from 'react-bootstrap';
import axios from 'axios';
import MyToast from './MyToast';
import API_BASE_URL from '../config/api';

class Voiture extends Component {
  initialState = {
    marque: '',
    modele: '',
    couleur: '',
    immatricule: '',
    annee: '',
    prix: '',
    proprietaireId: '',
    proprietaires: [],
    show: false
  };

  constructor(props) {
    super(props);
    this.state = this.initialState;
  }

  componentDidMount() {
    this.loadProprietaires();
  }

  loadProprietaires = () => {
    axios.get(`${API_BASE_URL}/proprietaires`)
      .then(response => {
        console.log("Proprietaires loaded:", response.data);
        // Filter out duplicates based on ID
        const uniqueProprietaires = response.data.filter((proprietaire, index, self) =>
          index === self.findIndex(p => p.id === proprietaire.id)
        );
        this.setState({ proprietaires: uniqueProprietaires });
        if (uniqueProprietaires.length > 0 && !this.state.proprietaireId) {
          this.setState({ proprietaireId: uniqueProprietaires[0].id.toString() });
        }
      })
      .catch(error => {
        console.error("Error loading proprietaires:", error);
        alert("Erreur lors du chargement des propriétaires: " + (error.response?.data?.message || error.message));
      });
  };

  voitureChange = (event) => {
    this.setState({
      [event.target.name]: event.target.value
    });
  };

  submitVoiture = (event) => {
    event.preventDefault();
    const { marque, modele, couleur, immatricule, annee, prix, proprietaireId } = this.state;
    
    // Validate that a proprietaire is selected
    if (!proprietaireId || proprietaireId === '') {
      alert("Veuillez sélectionner un propriétaire");
      return;
    }

    const proprietaire = this.state.proprietaires.find(p => p.id === parseInt(proprietaireId));
    
    if (!proprietaire) {
      alert("Propriétaire introuvable. Veuillez réessayer.");
      return;
    }

    const voiture = {
      marque: marque,
      modele: modele,
      couleur: couleur,
      immatricule: immatricule,
      annee: parseInt(annee),
      prix: parseInt(prix),
      proprietaire: {
        id: proprietaire.id,
        nom: proprietaire.nom,
        prenom: proprietaire.prenom
      }
    };

    console.log("Submitting voiture:", voiture);

    axios.post(`${API_BASE_URL}/voitures`, voiture)
      .then(response => {
        console.log("Response:", response);
        if (response.data != null) {
          // Reset form but keep proprietaires list
          this.setState({
            marque: '',
            modele: '',
            couleur: '',
            immatricule: '',
            annee: '',
            prix: '',
            proprietaireId: this.state.proprietaires.length > 0 ? this.state.proprietaires[0].id.toString() : '',
            show: true
          });
          setTimeout(() => this.setState({ show: false }), 3000);
        }
      })
      .catch(error => {
        console.error("Error saving voiture:", error);
        console.error("Error response:", error.response);
        alert("Erreur lors de l'enregistrement: " + (error.response?.data?.message || error.message));
        this.setState({ show: false });
      });
  };

  reset = () => {
    this.setState({
      marque: '',
      modele: '',
      couleur: '',
      immatricule: '',
      annee: '',
      prix: '',
      proprietaireId: this.state.proprietaires.length > 0 ? this.state.proprietaires[0].id.toString() : '',
      show: false
    });
  };

  render() {
    const { marque, modele, couleur, immatricule, prix, annee, proprietaireId, proprietaires } = this.state;

    return (
      <div>
        <div style={{ display: this.state.show ? "block" : "none" }}>
          <MyToast children={{ show: this.state.show, message: "Voiture enregistrée avec succès.", type: "success" }} />
        </div>

        <Card className="border border-dark bg-dark text-white">
          <Card.Header>
            <h3>Ajouter Voiture</h3>
          </Card.Header>
          <Form onSubmit={this.submitVoiture} onReset={this.reset}>
            <Card.Body>
              <Row>
                <FormGroup as={Col} controlId="formGridMarque">
                  <FormLabel>Marque</FormLabel>
                  <FormControl
                    required
                    value={marque}
                    name="marque"
                    autoComplete="off"
                    type="text"
                    onChange={this.voitureChange}
                    className="bg-dark text-white"
                    placeholder="Entrez Marque Voiture"
                  />
                </FormGroup>
                <FormGroup as={Col} controlId="formGridModele">
                  <FormLabel>Modele</FormLabel>
                  <FormControl
                    required
                    value={modele}
                    name="modele"
                    autoComplete="off"
                    type="text"
                    onChange={this.voitureChange}
                    className="bg-dark text-white"
                    placeholder="Entrez Modele Voiture"
                  />
                </FormGroup>
              </Row>
              <Row>
                <FormGroup as={Col} controlId="formGridCouleur">
                  <FormLabel>Couleur</FormLabel>
                  <FormControl
                    required
                    value={couleur}
                    name="couleur"
                    autoComplete="off"
                    type="text"
                    onChange={this.voitureChange}
                    className="bg-dark text-white"
                    placeholder="Entrez Couleur Voiture"
                  />
                </FormGroup>
                <FormGroup as={Col} controlId="formGridImmatricule">
                  <FormLabel>Immatricule</FormLabel>
                  <FormControl
                    required
                    value={immatricule}
                    name="immatricule"
                    autoComplete="off"
                    type="text"
                    onChange={this.voitureChange}
                    className="bg-dark text-white"
                    placeholder="Entrez Immatricule Voiture"
                  />
                </FormGroup>
              </Row>
              <Row>
                <FormGroup as={Col} controlId="formGridAnnee">
                  <FormLabel>Année</FormLabel>
                  <FormControl
                    required
                    value={annee}
                    name="annee"
                    autoComplete="off"
                    type="number"
                    onChange={this.voitureChange}
                    className="bg-dark text-white"
                    placeholder="Entrez Année Voiture"
                  />
                </FormGroup>
                <FormGroup as={Col} controlId="formGridPrix">
                  <FormLabel>Prix</FormLabel>
                  <FormControl
                    required
                    value={prix}
                    name="prix"
                    autoComplete="off"
                    type="number"
                    onChange={this.voitureChange}
                    className="bg-dark text-white"
                    placeholder="Entrez Prix Voiture"
                  />
                </FormGroup>
              </Row>
              <Row>
                <FormGroup as={Col} controlId="formGridProprietaire">
                  <FormLabel>Propriétaire</FormLabel>
                  <FormControl
                    as="select"
                    required
                    value={proprietaireId}
                    name="proprietaireId"
                    onChange={this.voitureChange}
                    className="bg-dark text-white"
                  >
                    {proprietaires.map(proprietaire => (
                      <option key={proprietaire.id} value={proprietaire.id.toString()}>
                        {proprietaire.nom} {proprietaire.prenom}
                      </option>
                    ))}
                  </FormControl>
                </FormGroup>
              </Row>
            </Card.Body>
            <Card.Footer style={{ textAlign: "right" }}>
              <Button size="sm" variant="success" type="submit">
                Submit
              </Button>
              <Button size="sm" variant="info" type="reset">
                Reset
              </Button>
            </Card.Footer>
          </Form>
        </Card>
      </div>
    );
  }
}

export default Voiture;

