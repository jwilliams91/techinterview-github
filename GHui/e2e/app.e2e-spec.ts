import { GhuiPage } from './app.po';

describe('ghui App', () => {
  let page: GhuiPage;

  beforeEach(() => {
    page = new GhuiPage();
  });

  it('should display message saying app works', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('app works!');
  });
});
