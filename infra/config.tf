# Copyright (c) TIKI Inc.
# MIT license. See LICENSE file in root directory.

locals {
  port     = 8544
  vpc_uuid = "0375b29c-32a0-4edf-86c5-5cdd33137540"
  region   = "nyc3"
}

variable "sem_ver" {}
variable "doppler_st" {}