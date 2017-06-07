# EVSelector

<img src="http://1.bp.blogspot.com/-cesKQfhNB4s/WTgRgAYaSUI/AAAAAAAAKJo/ss1mN31hPwQRo6YW4T1w7xgqGNEHfAOhACK4B/s1600/evselector.gif" width="300">

<code>
this.selector = (EVSelector)this.findViewById(R.id.select_dialog);

        int[] drawables = {
                R.mipmap.icon1,
                R.mipmap.icon2,
                R.mipmap.icon3
        };

        this.selector.setSelectIcon(drawables);

        this.selector.setListener(new EVSelector.IconSelectListener() {
            @Override
            public void onOpen() {
                selectedTextView.setText("");
            }

            @Override
            public void onSelected(int iconIndex) {
                selectedTextView.setText("Select  icon: " + iconIndex);
            }

            @Override
            public void onCancel() {
                selectedTextView.setText("Cancel");
            }
        });
</code>

