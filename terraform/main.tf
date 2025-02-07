module "ecr" {
  source = "./modules/repository"

  application = var.application
}

module "helm_deploy" {
  source                = "./modules/helm"
  helm_service_template = var.helm_service_template
  container_image_tag   = data.aws_ecr_repository.image_tag.most_recent_image_tags[0]
  container_image_url   = data.aws_ecr_repository.image_tag.repository_url
  ingress_config_host   = data.kubernetes_service.ingress_nginx.status.0.load_balancer.0.ingress.0.hostname
  config_map_name       = module.kubernetes_environment.config_map_name[0]
  secret_name           = module.kubernetes_environment.secret_name[0]
  secret_env_values     = ""

  depends_on = [
    module.kubernetes_environment
  ]
}

module "kubernetes_environment" {
  source                = "./modules/kubernetes"
  helm_service_template = var.helm_service_template
  kubernetes_secrets_data = {
    DATABASE_USER     = local.secret_data.username
    DATABASE_PASSWORD = local.secret_data.password
  }
}

locals {
  secret_data = jsondecode(data.aws_secretsmanager_secret_version.my_secret_version.secret_string)
}

output "name" {
  value     = module.kubernetes_environment.secret_data
  sensitive = true
}
