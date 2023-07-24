git checkout --orphan TEMP_BRANCH
git add -A
git commit -am "Initial commit"
git branch -D $1
git branch -m $1
git push -f origin $1