// Hand-maintained (not generated). The Editora ref the user guide is copied from
// when it is not the release tag.
//
// The guide ships inside the app, so it is cut with the release; a guide fix made
// after the tag would otherwise wait a whole release to reach this site. Such a
// fix lands on Editora master and is cherry-picked onto a branch named
// `guide/v<version>`, cut from the release tag. Set the name here and
// `check:generated` copies the guide (user-docs/) from that branch instead of the
// tag. It refuses a branch that does not descend from the tag or that changes
// anything outside user-docs/, so the commands, keys and What's new still come
// from the release itself.
//
// Set it back to null when the next release is regenerated.
export const guideRef: string | null = "guide/v0.29.0";
