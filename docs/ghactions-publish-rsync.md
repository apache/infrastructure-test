# Uploading to nightlies.apache.org via Github Actions via rsync

## This page was converted from md to html and then uploaded here from Github Actions - using rsync!

See [nightlies-rsync.yml](https://github.com/apache/infrastructure-test/blob/master/.github/workflows/nightlies-rsync.yml) for usage example.

File an INFRA ticket and state which Github repo you will use to have it whitelisted against the secrets.

Example:

    on:
      push:
    jobs:
      upload_to_nightlies:
        runs-on: ubuntu-latest
        steps:
        - uses: actions/checkout@master
          ... 
          some step(s) to build your docs for transfer
          ...
        - name: rsync
          uses: burnett01/rsync-deployments@0dc935cdecc5f5e571865e60d2a6cdc673704823
          with:
            switches: -rlptDvz
            path: path/to/docs/for/upload*
            remote_path: ${{ secrets.NIGHTLIES_RSYNC_PATH }}/YourProjectName
            remote_host: ${{ secrets.NIGHTLIES_RSYNC_HOST }}
            remote_port: ${{ secrets.NIGHTLIES_RSYNC_PORT }}
            remote_user: ${{ secrets.NIGHTLIES_RSYNC_USER }}
            remote_key: ${{ secrets.NIGHTLIES_RSYNC_KEY }}



Enjoy!
