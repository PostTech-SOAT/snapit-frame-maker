terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }

    helm = {
      source  = "hashicorp/helm"
      version = "2.9.0"
    }

    kubernetes = {
      source  = "hashicorp/kubernetes"
      version = "2.19.0"
    }

    kubectl = {
      source = "gavinbunney/kubectl"
    }
  }
}

provider "aws" {
  region = var.aws_region
  default_tags {
    tags = {
      Application = "${var.application}"
      Terraform   = "TRUE"
    }
  }

}

provider "kubernetes" {
  config_path = "~/.kube/config"
}

provider "helm" {
  kubernetes {
    config_path = "~/.kube/config"
  }
}
