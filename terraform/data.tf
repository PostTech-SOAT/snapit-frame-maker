data "aws_ecr_repository" "image_tag" {
  name = var.application

  depends_on = [module.ecr]
}

data "kubernetes_service" "ingress_nginx" {
  metadata {
    name      = var.ingress_nginx_name
    namespace = "kube-system"
  }
}

data "aws_secretsmanager_secret" "my_secret" {
  name = "sm-rds-snapit"
}

data "aws_secretsmanager_secret_version" "my_secret_version" {
  secret_id = data.aws_secretsmanager_secret.my_secret.id
}
