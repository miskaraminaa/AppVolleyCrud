<?php

use service\EtudiantService;

include_once '../racine.php';
include_once RACINE.'/service/EtudiantService.php';
extract($_POST);

$es = new EtudiantService();
$es->create(new Etudiant(1, $nom, $prenom, $ville, $sexe, $imageBase64));

header("location:../index.php");